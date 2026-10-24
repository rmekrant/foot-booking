import { useState } from 'react';

export default function ReservationForm({ terrain, date, heureDebut, enCours, onReserver }) {
  const [nom, setNom] = useState('');

  function soumettre(evenement) {
    evenement.preventDefault();
    onReserver(nom.trim());
  }

  if (!heureDebut) {
    return <p className="aide">Choisis un créneau libre pour réserver.</p>;
  }

  return (
    <form className="formulaire" onSubmit={soumettre}>
      <p>
        <strong>{terrain.nom}</strong>, le {date}, à {heureDebut}
      </p>
      <label htmlFor="nom">Ton nom</label>
      <div className="ligne">
        <input
          id="nom"
          value={nom}
          onChange={(e) => setNom(e.target.value)}
          maxLength={100}
          placeholder="Ex. : Ramzy"
          autoComplete="name"
        />
        <button type="submit" className="principal" disabled={enCours || nom.trim() === ''}>
          {enCours ? 'Réservation…' : 'Réserver'}
        </button>
      </div>
    </form>
  );
}
