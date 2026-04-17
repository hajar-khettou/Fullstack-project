package com.gameboard.config;

import com.gameboard.game.BoardGame;
import com.gameboard.game.BoardGameRepository;
import com.gameboard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final BoardGameRepository boardGameRepository;

    @Override
    public void run(ApplicationArguments args) {
        userService.initDefaultUsers();
        initDefaultGames();
    }

    private void initDefaultGames() {
        if (boardGameRepository.count() > 0) return;

        boardGameRepository.saveAll(List.of(
            game("Catan", "Colonisez une île en collectant et échangeant des ressources pour construire routes, villages et villes.", "https://cf.geekdo-images.com/W3Bsga_uLP9kO91gZ7H8yw__original/img/M_3Vg1j2HlNgkzBqMQCaGf5fBdk=/0x0/filters:format(jpeg)/pic2419375.jpg", "Stratégie", 3, 4, 1995),
            game("Ticket to Ride", "Reliez des villes à travers l'Europe en posant des wagons sur des routes ferroviaires.", "https://cf.geekdo-images.com/ZWJg0dCdrWHspVJym5GSsg__original/img/U1gy64tDRBSNKCfkHfG6EBiI-PQ=/0x0/filters:format(jpeg)/pic38668.jpg", "Famille", 2, 5, 2004),
            game("Pandemic", "Coopérez pour stopper quatre maladies qui se répandent sur la planète avant qu'il ne soit trop tard.", "https://cf.geekdo-images.com/S3plyFn2eS0HZkANzjCpyQ__original/img/snU0Ykr8bUYsYDqDSqrWk0Xs5JI=/0x0/filters:format(jpeg)/pic1534148.jpg", "Coopératif", 2, 4, 2008),
            game("Les Aventuriers du Rail", "Parcourez l'Amérique du Nord en construisant des lignes de train pour relier les grandes villes.", "https://cf.geekdo-images.com/ZWJg0dCdrWHspVJym5GSsg__original/img/U1gy64tDRBSNKCfkHfG6EBiI-PQ=/0x0/filters:format(jpeg)/pic38668.jpg", "Famille", 2, 5, 2004),
            game("7 Wonders", "Développez votre civilisation antique en construisant des merveilles et en gérant votre économie.", "https://cf.geekdo-images.com/RvFVTEpnbb4NM7k0IF8V7A__original/img/PTKnCMFVKlD89fBBPOtg7HJaMPw=/0x0/filters:format(jpeg)/pic860217.jpg", "Stratégie", 2, 7, 2010),
            game("Agricola", "Gérez une ferme médiévale, nourrissez votre famille et développez vos terres.", "https://cf.geekdo-images.com/dDDo2SOdgd5Y9TV1XR6xFg__original/img/3K0v1MtJTjhQK8GbrH7UkGCnUy8=/0x0/filters:format(jpeg)/pic831744.jpg", "Gestion", 1, 5, 2007),
            game("Dominion", "Construisez votre deck de cartes pour dominer le royaume en achetant actions et trésors.", "https://cf.geekdo-images.com/j6iQpZ4XkemZP07HNCODBA__original/img/xGFSHITsXqzSWHDjVAaxINHRFcU=/0x0/filters:format(jpeg)/pic394356.jpg", "Cartes", 2, 4, 2008),
            game("Carcassonne", "Posez des tuiles pour construire un paysage médiéval et déployez vos partisans pour marquer des points.", "https://cf.geekdo-images.com/okSM2hCBhHMQGF6f7Bel9A__original/img/TacqJDKYXF2QrwKxKbJeP6i5vdU=/0x0/filters:format(jpeg)/pic2337577.jpg", "Famille", 2, 5, 2000),
            game("Wingspan", "Attirez des oiseaux dans vos réserves naturelles pour créer un moteur de jeu puissant.", "https://cf.geekdo-images.com/yLZJCVLlIx4c7eJEWUNJ7w__original/img/uIjeoKgHMcRtzRSR4MXdGAONrgQ=/0x0/filters:format(jpeg)/pic4458123.jpg", "Stratégie", 1, 5, 2019),
            game("Azul", "Carrelez le mur du palais royal en collectant des tuiles colorées avec élégance.", "https://cf.geekdo-images.com/aPSHJO0d0XOpQR5X-wJonw__original/img/4Gj4xUxGPSeTvZNBOSjTPFwMZi8=/0x0/filters:format(jpeg)/pic3718275.jpg", "Abstrait", 2, 4, 2017),
            game("Root", "Jouez des factions asymétriques en guerre pour le contrôle d'une forêt.", "https://cf.geekdo-images.com/JUAUWaVUzeBgzirhZNmHHw__original/img/IHX2-pFPrDlFhFbDSdJBz81TuLk=/0x0/filters:format(jpeg)/pic4254509.jpg", "Stratégie", 2, 4, 2018),
            game("Gloomhaven", "Incarnez des mercenaires dans un donjon coopératif avec une campagne narrative profonde.", "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__original/img/7d-lj5Gd1e8PFnD97sCzgQS4SoA=/0x0/filters:format(jpeg)/pic2437871.jpg", "Coopératif", 1, 4, 2017),
            game("Terraforming Mars", "Transformez la planète rouge en colonisant ses territoires, gérant eau, température et oxygène.", "https://cf.geekdo-images.com/wg9oOLcsKvDesSUdZQ4rxw__original/img/BTi6CMbKLIQT7eJjYfIjJqeFpnw=/0x0/filters:format(jpeg)/pic3536616.jpg", "Stratégie", 1, 5, 2016),
            game("Betrayal at House on the Hill", "Explorez une maison hantée jusqu'à ce qu'un traître se révèle parmi vous.", "https://cf.geekdo-images.com/d0Bxr5TMtFFBwjMXCDqNYQ__original/img/0RQFM3DhRCB6HuVS6JIk_MxeBkA=/0x0/filters:format(jpeg)/pic828598.jpg", "Horreur", 3, 6, 2004),
            game("Splendor", "Collectez des gemmes pour acheter des cartes et attirer des nobles dans ce jeu de développement.", "https://cf.geekdo-images.com/rwOMxx3q5yuShFygAx9mGA__original/img/qVBj7GcVHqaUARMXFsiIq-F9rlM=/0x0/filters:format(jpeg)/pic1904079.jpg", "Stratégie", 2, 4, 2014)
        ));
    }

    private BoardGame game(String title, String description, String imageUrl, String genre, int min, int max, int year) {
        BoardGame g = new BoardGame();
        g.setTitle(title);
        g.setDescription(description);
        g.setImageUrl(imageUrl);
        g.setGenre(genre);
        g.setMinPlayers(min);
        g.setMaxPlayers(max);
        g.setYear(year);
        g.setStatus(BoardGame.Status.APPROVED);
        g.setProposedBy("admin");
        return g;
    }
}
