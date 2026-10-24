import { useEffect, useState } from 'react';
import { annulerReservation, listerCreneaux, listerTerrains, reserver } from './api/api.js';
import TerrainList from './components/TerrainList.jsx';
import CreneauList from './components/CreneauList.jsx';
import ReservationForm from './components/ReservationForm.jsx';
import MesReservations from './components/MesReservations.jsx';
import { aujourdhui, formaterDate } from './utils/date.js';
import {
  chargerMesReservations,
  enregistrerMesReservations,
  trierReservations,
} from './utils/mesReservations.js';

export default function App() {
  const [terrains, setTerrains] = useState([]);
  const [chargementTerrains, setChargementTerrains] = useState(true);
  const [terrain, setTerrain] = useState(null);
  const [date, setDate] = useState(aujourdhui());
  const [creneaux, setCreneaux] = useState([]);
  const [chargementCreneaux, setChargementCreneaux] = useState(false);
  const [creneauSelectionne, setCreneauSelectionne] = useState(null);
  const [rafraichissement, setRafraichissement] = useState(0);
  const [reservationEnCours, setReservationEnCours] = useState(false);
  const [mesReservations, setMesReservations] = useState(chargerMesReservations);
  const [idEnAnnulation, setIdEnAnnulation] = useState(null);
  const [message, setMessage] = useState(null); // { type: 'succes' | 'erreur', texte }

  // Chargement des terrains au démarrage
  useEffect(() => {
    listerTerrains()
      .then(setTerrains)
      .catch((erreur) => setMessage({ type: 'erreur', texte: erreur.message }))
      .finally(() => setChargementTerrains(false));
  }, []);

  // Sauvegarde de « Mes réservations » à chaque modification
  useEffect(() => {
    enregistrerMesReservations(mesReservations);
  }, [mesReservations]);

  // Chargement des créneaux quand le terrain, la date ou le compteur de rafraîchissement change
  useEffect(() => {
    if (!terrain || !date) return;

    // Si l'utilisateur change de terrain avant la fin de la requête,
    // on ignore l'ancienne réponse pour ne pas afficher les créneaux du mauvais terrain.
    let ignorer = false;
    setChargementCreneaux(true);
    listerCreneaux(terrain.id, date)
      .then((donnees) => {
        if (!ignorer) setCreneaux(donnees);
      })
      .catch((erreur) => {
        if (!ignorer) {
          setCreneaux([]);
          setMessage({ type: 'erreur', texte: erreur.message });
        }
      })
      .finally(() => {
        if (!ignorer) setChargementCreneaux(false);
      });

    return () => {
      ignorer = true;
    };
  }, [terrain, date, rafraichissement]);

  function rafraichirCreneaux() {
    setRafraichissement((n) => n + 1);
  }

  function choisirTerrain(nouveauTerrain) {
    setTerrain(nouveauTerrain);
    setCreneauSelectionne(null);
    setMessage(null);
  }

  function choisirDate(nouvelleDate) {
    setDate(nouvelleDate);
    setCreneauSelectionne(null);
    setMessage(null);
  }

  async function confirmerReservation(nomClient) {
    setReservationEnCours(true);
    setMessage(null);
    try {
      const reservation = await reserver({
        terrainId: terrain.id,
        date,
        heureDebut: creneauSelectionne,
        nomClient,
      });
      setMesReservations((liste) => trierReservations([...liste, reservation]));
      setMessage({
        type: 'succes',
        texte: `Réservation n°${reservation.id} confirmée : ${reservation.terrainNom}, ${formaterDate(reservation.date)} de ${reservation.heureDebut} à ${reservation.heureFin}.`,
      });
    } catch (erreur) {
      setMessage({ type: 'erreur', texte: erreur.message });
    } finally {
      setReservationEnCours(false);
      setCreneauSelectionne(null);
      // Dans tous les cas, on recharge : en cas de 409, quelqu'un a pris le créneau entre-temps.
      rafraichirCreneaux();
    }
  }

  async function annuler(reservation) {
    setIdEnAnnulation(reservation.id);
    setMessage(null);
    try {
      await annulerReservation(reservation.id);
      setMessage({ type: 'succes', texte: `Réservation n°${reservation.id} annulée.` });
    } catch (erreur) {
      if (erreur.status !== 404) {
        setMessage({ type: 'erreur', texte: erreur.message });
        return;
      }
      // 404 : la réservation n'existe déjà plus (annulée ailleurs) → on la retire simplement.
      setMessage({ type: 'succes', texte: `La réservation n°${reservation.id} avait déjà été annulée.` });
    } finally {
      setIdEnAnnulation(null);
    }
    setMesReservations((liste) => liste.filter((r) => r.id !== reservation.id));
    rafraichirCreneaux();
  }

  return (
    <div className="page">
      <header className="entete">
        <h1>⚽ FootBooking</h1>
        <p>Réserve un terrain de foot en quelques clics.</p>
      </header>

      {message && (
        <div className={`message ${message.type}`} role={message.type === 'erreur' ? 'alert' : 'status'}>
          {message.texte}
        </div>
      )}

      <main className="contenu">
        <section className="carte">
          <h2>1. Choisis un terrain</h2>
          {chargementTerrains ? (
            <p className="aide">Chargement…</p>
          ) : (
            <TerrainList
              terrains={terrains}
              terrainSelectionneId={terrain?.id}
              onSelection={choisirTerrain}
            />
          )}
        </section>

        <section className="carte">
          <h2>2. Choisis un créneau</h2>
          {!terrain ? (
            <p className="aide">Sélectionne d'abord un terrain.</p>
          ) : (
            <>
              <label className="champ-date">
                Date
                <input
                  type="date"
                  value={date}
                  min={aujourdhui()}
                  onChange={(e) => choisirDate(e.target.value)}
                />
              </label>
              {chargementCreneaux ? (
                <p className="aide">Chargement des créneaux…</p>
              ) : (
                <CreneauList
                  creneaux={creneaux}
                  creneauSelectionne={creneauSelectionne}
                  onSelection={setCreneauSelectionne}
                />
              )}
              <ReservationForm
                terrain={terrain}
                date={formaterDate(date)}
                heureDebut={creneauSelectionne}
                enCours={reservationEnCours}
                onReserver={confirmerReservation}
              />
            </>
          )}
        </section>

        <section className="carte pleine-largeur">
          <h2>Mes réservations</h2>
          <MesReservations
            reservations={mesReservations}
            idEnAnnulation={idEnAnnulation}
            onAnnuler={annuler}
          />
        </section>
      </main>
    </div>
  );
}
