package benchmark;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.facility_rental.FacilityRentalApplication;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.data.ReMoUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BTest1 {
    private ConfigurableApplicationContext context;
    private ReMoUserRepository repo;
    private List<String> ids = new ArrayList();

    @Setup(Level.Trial)
    public void init() {
        // Startuje cały Spring Boot (lub tylko konfigurację)
        context = new SpringApplicationBuilder(FacilityRentalApplication.class)
                .profiles("test")
                .run();

        // Pobranie gotowego beana z wstrzykniętymi zależnościami
        repo = context.getBean(ReMoUserRepository.class);

        // Czyścimy cache
        repo.clearCache();

        List<User> list = repo.findAll();
        for (User u : list) {
            repo.delete(u.getId());
        }

        repo.clearCache();

        // Przygotowujemy dane w Mongo
        repo.save(new Client("Albertus", "Alb@a.pl",
                true, "Albert", "Alanowicz", "111"));
        repo.save(new Client("Jankos", "gigagracz@lol.us",
                true, "Janek", "Pomidorowy", "123"));
        repo.save(new Client("Kubica", "szybki@robert.pl",
                true, "Tomek", "Szybki", "4444"));

        repo.getAllClients().forEach(c -> ids.add(c.getId()));
    }

    @Setup(Level.Invocation)
    public void beforeEach() {
        fillCache();
    }

    public void fillCache() {
        repo.findById(ids.get(0));
        repo.findById(ids.get(1));
        repo.findById(ids.get(2));
    }

    @Benchmark
    public void dbSpeedTest() {
        repo.clearCache();
        repo.findById(ids.get(0));
        repo.findById(ids.get(1));
        repo.findById(ids.get(2));
    }

    @Benchmark
    public void chacheSpeedTest() {
        fillCache();
        repo.findById(ids.get(0));
        repo.findById(ids.get(1));
        repo.findById(ids.get(2));
    }

    @Benchmark
    public void clearCacheTest() {
        repo.clearCache();
    }

    @Benchmark
    public void fillCacheTest() {
        fillCache();
    }
}
