package org.deus.src.seeders;

import org.deus.src.models.GenreModel;
import org.deus.src.repositories.GenreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class GenreSeeder implements CommandLineRunner {
    private final GenreRepository genreRepository;

    public GenreSeeder(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void run(String... args) {
        if (genreRepository.count() == 0) {
            List<GenreModel> genres = Stream.of(
                            new GenreModel(null, "Rock", null),
                            new GenreModel(null, "Pop", null),
                            new GenreModel(null, "Hip-Hop", null),
                            new GenreModel(null, "Jazz", null),
                            new GenreModel(null, "Classical", null),
                            new GenreModel(null, "Electronic", null),
                            new GenreModel(null, "Blues", null),
                            new GenreModel(null, "Country", null),
                            new GenreModel(null, "Reggae", null),
                            new GenreModel(null, "Metal", null),
                            new GenreModel(null, "Disco", null),
                            new GenreModel(null, "Funk", null),
                            new GenreModel(null, "House", null),
                            new GenreModel(null, "Techno", null),
                            new GenreModel(null, "Dubstep", null),
                            new GenreModel(null, "Trance", null),
                            new GenreModel(null, "R&B", null),
                            new GenreModel(null, "Soul", null),
                            new GenreModel(null, "Gospel", null),
                            new GenreModel(null, "Ska", null),
                            new GenreModel(null, "Punk", null),
                            new GenreModel(null, "Emo", null),
                            new GenreModel(null, "Indie Rock", null),
                            new GenreModel(null, "Alternative Rock", null),
                            new GenreModel(null, "Grunge", null),
                            new GenreModel(null, "Post-Rock", null),
                            new GenreModel(null, "Post-Punk", null),
                            new GenreModel(null, "Hardcore", null),
                            new GenreModel(null, "K-pop", null),
                            new GenreModel(null, "J-pop", null),
                            new GenreModel(null, "Latin", null),
                            new GenreModel(null, "Salsa", null),
                            new GenreModel(null, "Reggaeton", null),
                            new GenreModel(null, "Bossa Nova", null),
                            new GenreModel(null, "Flamenco", null),
                            new GenreModel(null, "Tango", null),
                            new GenreModel(null, "Fado", null),
                            new GenreModel(null, "Ambient", null),
                            new GenreModel(null, "Chillout", null),
                            new GenreModel(null, "Lounge", null),
                            new GenreModel(null, "Trip-Hop", null),
                            new GenreModel(null, "Drum and Bass", null),
                            new GenreModel(null, "Breakbeat", null),
                            new GenreModel(null, "Garage", null),
                            new GenreModel(null, "Grime", null),
                            new GenreModel(null, "Trap", null),
                            new GenreModel(null, "Synthpop", null),
                            new GenreModel(null, "New Wave", null),
                            new GenreModel(null, "Progressive Rock", null),
                            new GenreModel(null, "Psychedelic Rock", null),
                            new GenreModel(null, "Shoegaze", null),
                            new GenreModel(null, "Dream Pop", null),
                            new GenreModel(null, "Avant-garde", null),
                            new GenreModel(null, "Experimental", null),
                            new GenreModel(null, "Noise", null),
                            new GenreModel(null, "Industrial", null),
                            new GenreModel(null, "Folk", null),
                            new GenreModel(null, "Bluegrass", null),
                            new GenreModel(null, "Celtic", null),
                            new GenreModel(null, "Americana", null),
                            new GenreModel(null, "World Music", null),
                            new GenreModel(null, "Afrobeat", null),
                            new GenreModel(null, "Highlife", null),
                            new GenreModel(null, "Makossa", null),
                            new GenreModel(null, "Zouk", null),
                            new GenreModel(null, "Calypso", null),
                            new GenreModel(null, "Soca", null),
                            new GenreModel(null, "Merengue", null),
                            new GenreModel(null, "Bachata", null),
                            new GenreModel(null, "Bolero", null),
                            new GenreModel(null, "Mariachi", null),
                            new GenreModel(null, "Ranchera", null),
                            new GenreModel(null, "Norteño", null),
                            new GenreModel(null, "Cumbia", null),
                            new GenreModel(null, "Dancehall", null),
                            new GenreModel(null, "Chiptune", null),
                            new GenreModel(null, "Video Game Music", null),
                            new GenreModel(null, "Film Score", null),
                            new GenreModel(null, "Soundtrack", null),
                            new GenreModel(null, "Musical Theater", null),
                            new GenreModel(null, "Opera", null),
                            new GenreModel(null, "Baroque", null),
                            new GenreModel(null, "Renaissance", null),
                            new GenreModel(null, "Romantic", null),
                            new GenreModel(null, "Minimalism", null),
                            new GenreModel(null, "Neo-Classical", null),
                            new GenreModel(null, "Avant-garde Jazz", null),
                            new GenreModel(null, "Smooth Jazz", null),
                            new GenreModel(null, "Big Band", null),
                            new GenreModel(null, "Swing", null),
                            new GenreModel(null, "Bebop", null),
                            new GenreModel(null, "Latin Jazz", null),
                            new GenreModel(null, "Free Jazz", null),
                            new GenreModel(null, "Hard Bop", null),
                            new GenreModel(null, "Cool Jazz", null)
                    )
                    .sorted(Comparator.comparing(GenreModel::getName))
                    .toList();

            genreRepository.saveAll(genres);
            System.out.println("Table 'genres' was successfully filled.");
        } else {
            System.out.println("Table 'genres' is already filled.");
        }
    }
}
