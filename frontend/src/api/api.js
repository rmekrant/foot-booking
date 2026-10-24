// Toutes les communications avec le back-end passent par ce fichier.
// Les composants ne connaissent ni les URL ni le format des erreurs.

export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.status = status;
  }
}

async function requete(chemin, options = {}) {
  let reponse;
  try {
    reponse = await fetch(`/api${chemin}`, {
      ...options,
      headers: { 'Content-Type': 'application/json', ...options.headers },
    });
  } catch {
    throw new ApiError('Impossible de contacter le serveur.', 0);
  }

  if (!reponse.ok) {
    throw new ApiError(await lireMessageErreur(reponse), reponse.status);
  }
  return reponse.status === 204 ? null : reponse.json();
}

// Le back-end renvoie ses erreurs au format ProblemDetail (RFC 9457) :
// { status, title, detail, erreurs? }
async function lireMessageErreur(reponse) {
  try {
    const probleme = await reponse.json();
    if (probleme.erreurs) {
      return Object.values(probleme.erreurs).join(' · ');
    }
    if (probleme.detail) {
      return probleme.detail;
    }
  } catch {
    // corps vide ou non JSON
  }
  if (reponse.status >= 500) {
    return 'Le serveur ne répond pas. Le back-end est-il lancé ?';
  }
  return `Erreur ${reponse.status}`;
}

export function listerTerrains() {
  return requete('/terrains');
}

export function listerCreneaux(terrainId, date) {
  return requete(`/terrains/${terrainId}/creneaux?date=${date}`);
}

export function reserver({ terrainId, date, heureDebut, nomClient }) {
  return requete('/reservations', {
    method: 'POST',
    body: JSON.stringify({ terrainId, date, heureDebut, nomClient }),
  });
}

export function annulerReservation(id) {
  return requete(`/reservations/${id}`, { method: 'DELETE' });
}
