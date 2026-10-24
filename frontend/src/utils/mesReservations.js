// Sans comptes utilisateurs (V1), le navigateur garde la liste des réservations
// qu'il a faites dans le localStorage, pour pouvoir les annuler après un rechargement.
// Avec Spring Security, ces réservations seront rattachées à l'utilisateur connecté.

import { aujourdhui } from './date.js';

const CLE = 'footbooking.mesReservations';

export function chargerMesReservations() {
  try {
    const brut = localStorage.getItem(CLE);
    const reservations = brut ? JSON.parse(brut) : [];
    if (!Array.isArray(reservations)) return [];
    // On ne garde que les réservations d'aujourd'hui ou à venir.
    return reservations.filter((r) => r.date >= aujourdhui());
  } catch {
    // localStorage indisponible (navigation privée stricte) ou contenu corrompu
    return [];
  }
}

export function enregistrerMesReservations(reservations) {
  try {
    localStorage.setItem(CLE, JSON.stringify(reservations));
  } catch {
    // pas de stockage possible : la liste vivra seulement jusqu'au rechargement
  }
}

// Tri par date puis par heure (les formats AAAA-MM-JJ et HH:mm se trient comme du texte)
export function trierReservations(reservations) {
  return [...reservations].sort((a, b) =>
    `${a.date} ${a.heureDebut}`.localeCompare(`${b.date} ${b.heureDebut}`),
  );
}
