const LIBELLES_TYPE = { FOOT_5: 'Foot à 5', FOOT_7: 'Foot à 7' };

export default function TerrainList({ terrains, terrainSelectionneId, onSelection }) {
  if (terrains.length === 0) {
    return <p className="vide">Aucun terrain disponible.</p>;
  }

  return (
    <ul className="terrains">
      {terrains.map((terrain) => (
        <li key={terrain.id}>
          <button
            type="button"
            className={`terrain ${terrain.id === terrainSelectionneId ? 'selectionne' : ''}`}
            onClick={() => onSelection(terrain)}
          >
            <span className="terrain-nom">{terrain.nom}</span>
            <span className="terrain-adresse">{terrain.adresse}</span>
            <span className="badge">{LIBELLES_TYPE[terrain.type] ?? terrain.type}</span>
          </button>
        </li>
      ))}
    </ul>
  );
}
