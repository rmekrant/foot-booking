import { formaterDate } from '../utils/date.js';

export default function MesReservations({ reservations, idEnAnnulation, onAnnuler }) {
  if (reservations.length === 0) {
    return <p className="aide">Aucune réservation à venir faite depuis ce navigateur.</p>;
  }

  return (
    <ul className="mes-reservations">
      {reservations.map((r) => (
        <li key={r.id} className="ma-reservation">
          <div>
            <span className="ma-reservation-titre">{r.terrainNom}</span>
            <span className="ma-reservation-detail">
              {formaterDate(r.date)}, {r.heureDebut} – {r.heureFin} · au nom de {r.nomClient} · n°{r.id}
            </span>
          </div>
          <button
            type="button"
            className="secondaire"
            disabled={idEnAnnulation === r.id}
            onClick={() => onAnnuler(r)}
          >
            {idEnAnnulation === r.id ? 'Annulation…' : 'Annuler'}
          </button>
        </li>
      ))}
    </ul>
  );
}
