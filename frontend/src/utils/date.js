// Renvoie la date du jour au format AAAA-MM-JJ, dans le fuseau de l'utilisateur.
// (new Date().toISOString() donnerait la date UTC : faux entre minuit et 2 h à Paris.)
export function aujourdhui() {
  const d = new Date();
  const mois = String(d.getMonth() + 1).padStart(2, '0');
  const jour = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}-${mois}-${jour}`;
}

export function formaterDate(iso) {
  return new Date(`${iso}T00:00:00`).toLocaleDateString('fr-FR', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
  });
}
