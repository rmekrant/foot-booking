const LIBELLES_STATUT = { DISPONIBLE: 'Libre', RESERVE: 'Réservé', PASSE: 'Passé' };

export default function CreneauList({ creneaux, creneauSelectionne, onSelection }) {
  if (creneaux.length === 0) {
    return <p className="vide">Aucun créneau pour cette date.</p>;
  }

  return (
    <div className="creneaux">
      {creneaux.map((creneau) => {
        const disponible = creneau.statut === 'DISPONIBLE';
        const selectionne = creneau.heureDebut === creneauSelectionne;
        return (
          <button
            key={creneau.heureDebut}
            type="button"
            className={`creneau ${creneau.statut.toLowerCase()} ${selectionne ? 'selectionne' : ''}`}
            disabled={!disponible}
            onClick={() => onSelection(creneau.heureDebut)}
          >
            <span className="creneau-heure">
              {creneau.heureDebut} – {creneau.heureFin}
            </span>
            <span className="creneau-statut">{LIBELLES_STATUT[creneau.statut]}</span>
          </button>
        );
      })}
    </div>
  );
}
