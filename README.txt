Projet de CPO S6
version = 0

Pour prendre en main le Git : 

La première fois : 
git config --global user.name "Name"
git config --global user.email "adresse@email.com"
git clone https://github.com/zillioxkassandra/diet-improvement/
git checkout -b VotrePrenom

Chaque séance
git pull origin main (pour récupérer les modifications partagées les plus récentes)
git status 
git add fichier (ou . pour tous)
git commit -m "Message" (mettre un message explicite svp, permet de sauvegarder sur le dépot local)
git push origin VotreNom  (sauvegarder sur le dépot distant)

Quand vous avez une fonctionnalité complétée : 
git push origin main
