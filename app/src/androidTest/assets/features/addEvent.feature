# language: fr

Fonctionnalité: Ajouter un evenement
  En tant que chef de projet evenementiel
  Je veux ajouter un evenement
  Afin de pouvoir le planifier

  Plan du Scénario: Ajouter plusieurs evenements
    Étant donné un chef de projet evenementiel utilise l'application evenementielle
    Quand il appuie sur le bouton ajout evenement
    Et il ajoute "<Titre>" comme titre
    Et il ajoute "<Description>" comme description
    Et il selectionne "<Date>" comme date de debut
    Et il selectionne "<Heure>" comme heure de debut
    Et il appuie sur le bouton selectionner image
    Et il selectionne l'image numero <Image>
    Alors le nouvel evenement est dans la liste des evenements

    Exemples:
      | Titre                | Description                  | Date        | Heure  | Image |
      | Conference Tech      | Salon de la Tech 2025       | 10/06/2025  | 14:00  | 1     |
      | Atelier DevOps      | Formation CI/CD             | 12/06/2025  | 09:30  | 2     |
      | Lancement Produit   | Presentation du nouveau X   | 15/06/2025  | 18:00  | 3     |
      | Reunion strategique | Planification Q3            | 18/06/2025  | 10:00  | 4     |
      | Meetup JavaScript   | Discussions JS avancees     | 20/06/2025  | 16:30  | 5     |
      | Hackathon IA        | Concours d'intelligence art. | 25/06/2025  | 08:00  | 6     |
      | Webinaire Securite  | Formation en cybersecurite  | 28/06/2025  | 11:45  | 7     |
      | Workshop UX         | Optimisation de l'UX        | 30/06/2025  | 14:15  | 8     |
      | Seance de reseautage | Rencontre avec experts      | 02/07/2025  | 17:00  | 9     |
      | Forum e-commerce    | Conference sur le e-commerce | 05/07/2025  | 13:00  | 10    |