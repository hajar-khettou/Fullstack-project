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
            game("Catan", "Colonisez une île en collectant et échangeant des ressources pour construire routes, villages et villes.", "https://upload.wikimedia.org/wikipedia/en/a/a3/Catan-2015-boxart.jpg", "Stratégie", 3, 4, 1995),
            game("Ticket to Ride", "Reliez des villes à travers l'Europe en posant des wagons sur des routes ferroviaires.", "https://upload.wikimedia.org/wikipedia/en/9/92/Ticket_to_Ride_Board_Game_Box_EN.jpg", "Famille", 2, 5, 2004),
            game("Pandemic", "Coopérez pour stopper quatre maladies qui se répandent sur la planète avant qu'il ne soit trop tard.", "https://upload.wikimedia.org/wikipedia/en/2/2a/PandemicBoard.jpg", "Coopératif", 2, 4, 2008),
            game("Les Aventuriers du Rail", "Parcourez l'Amérique du Nord en construisant des lignes de train pour relier les grandes villes.", "https://upload.wikimedia.org/wikipedia/en/9/92/Ticket_to_Ride_Board_Game_Box_EN.jpg", "Famille", 2, 5, 2004),
            game("7 Wonders", "Développez votre civilisation antique en construisant des merveilles et en gérant votre économie.", "https://upload.wikimedia.org/wikipedia/en/7/7c/7_wonders_board_game_cover.jpg", "Stratégie", 2, 7, 2010),
            game("Agricola", "Gérez une ferme médiévale, nourrissez votre famille et développez vos terres.", "https://upload.wikimedia.org/wikipedia/en/f/f4/Agricola_board_game_box_cover.jpg", "Gestion", 1, 5, 2007),
            game("Dominion", "Construisez votre deck de cartes pour dominer le royaume en achetant actions et trésors.", "https://upload.wikimedia.org/wikipedia/en/7/7f/Dominion_game_cover.jpg", "Cartes", 2, 4, 2008),
            game("Carcassonne", "Posez des tuiles pour construire un paysage médiéval et déployez vos partisans pour marquer des points.", "https://upload.wikimedia.org/wikipedia/en/a/a8/Carcassonne_game.jpg", "Famille", 2, 5, 2000),
            game("Wingspan", "Attirez des oiseaux dans vos réserves naturelles pour créer un moteur de jeu puissant.", "https://upload.wikimedia.org/wikipedia/en/d/db/Wingspan_board_game.jpg", "Stratégie", 1, 5, 2019),
            game("Azul", "Carrelez le mur du palais royal en collectant des tuiles colorées avec élégance.", "https://upload.wikimedia.org/wikipedia/en/9/9e/Azul_board_game_box_cover.jpg", "Abstrait", 2, 4, 2017),
            game("Root", "Jouez des factions asymétriques en guerre pour le contrôle d'une forêt.", "https://upload.wikimedia.org/wikipedia/en/5/51/Root_board_game_cover.jpg", "Stratégie", 2, 4, 2018),
            game("Gloomhaven", "Incarnez des mercenaires dans un donjon coopératif avec une campagne narrative profonde.", "https://upload.wikimedia.org/wikipedia/en/3/3e/Gloomhaven_box_cover.jpg", "Coopératif", 1, 4, 2017),
            game("Terraforming Mars", "Transformez la planète rouge en colonisant ses territoires, gérant eau, température et oxygène.", "https://upload.wikimedia.org/wikipedia/en/f/fc/Terraforming_Mars_board_game_box_cover.jpg", "Stratégie", 1, 5, 2016),
            game("Betrayal at House on the Hill", "Explorez une maison hantée jusqu'à ce qu'un traître se révèle parmi vous.", "https://upload.wikimedia.org/wikipedia/en/0/09/Betrayal_at_house_on_the_hill_cover.jpg", "Horreur", 3, 6, 2004),
            game("Splendor", "Collectez des gemmes pour acheter des cartes et attirer des nobles dans ce jeu de développement.", "https://upload.wikimedia.org/wikipedia/en/2/2e/Splendor_board_game_cover.jpg", "Stratégie", 2, 4, 2014)
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
