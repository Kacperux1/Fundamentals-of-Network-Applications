//package pl.facility_rental.data.mongo;
//
//
//import org.junit.Test;
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.results.format.ResultFormatType;
//import org.openjdk.jmh.runner.Runner;
//import org.openjdk.jmh.runner.RunnerException;
//import org.openjdk.jmh.runner.options.Options;
//import org.openjdk.jmh.runner.options.OptionsBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.junit.runner.RunWith;
//import pl.facility_rental.user.business.model.Client;
//import pl.facility_rental.user.business.model.User;
//import pl.facility_rental.user.data.ReMoUserRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@SpringBootTest
//@State(Scope.Benchmark)
//@BenchmarkMode(Mode.AverageTime)
//@OutputTimeUnit(TimeUnit.MICROSECONDS)
//@RunWith(SpringRunner.class)
//public class BenchmarkTest {
//
//    private List<String> ids = new ArrayList();
//
//    private final static Integer MEASUREMENT_ITERATIONS = 3;
//    private final static Integer WARMUP_ITERATIONS = 3;
//
//    @Test
//    public void executeJmhRunner() throws RunnerException {
//        Options opt = new OptionsBuilder()
//                // set the class name regex for benchmarks to search for to the current class
//                .include("\\." + this.getClass().getSimpleName() + "\\.")
//                .warmupIterations(WARMUP_ITERATIONS)
//                .measurementIterations(MEASUREMENT_ITERATIONS)
//                // do not use forking or the benchmark methods will not see references stored within its class
//                .forks(1)
//                // do not use multiple threads
//                .threads(1)
//                .shouldDoGC(true)
//                .shouldFailOnError(true)
//                .resultFormat(ResultFormatType.JSON)
//                .result("./results.txt") // set this to a valid filename if you want reports
//                .shouldFailOnError(true)
//                .jvmArgs("-server")
//                .build();
//
//        System.out.println(new Runner(opt).run());
//    }
//
//    @Autowired
//    private ReMoUserRepository repo;
//
//
//    @Setup(Level.Trial)
//    public void init() {
//        List<User> list = repo.findAll();
//        for (User u : list) {
//            repo.delete(u.getId());
//        }
//
//        repo.clearCache();
//
//        // Przygotowujemy dane w Mongo
//        repo.save(new Client("Albertus", "Alb@a.pl",
//                true, "Albert", "Alanowicz", "111"));
//        repo.save(new Client("Jankos", "gigagracz@lol.us",
//                true, "Janek", "Pomidorowy", "123"));
//        repo.save(new Client("Kubica", "szybki@robert.pl",
//                true, "Tomek", "Szybki", "4444"));
//
//        repo.getAllClients().forEach(c -> ids.add(c.getId()));
//    }
//
//    @Setup(Level.Invocation)
//    public void beforeEach() {
//        fillCache();
//    }
//
//    public void fillCache() {
//        repo.findById(ids.get(0));
//        repo.findById(ids.get(1));
//        repo.findById(ids.get(2));
//    }
//
//    @Benchmark
//    public void dbSpeedTest() {
//        repo.clearCache();
//        repo.findById(ids.get(0));
//        repo.findById(ids.get(1));
//        repo.findById(ids.get(2));
//    }
//
//    @Benchmark
//    public void chacheSpeedTest() {
//        fillCache();
//        repo.findById(ids.get(0));
//        repo.findById(ids.get(1));
//        repo.findById(ids.get(2));
//    }
//
//    @Benchmark
//    public void clearCacheTest() {
//        repo.clearCache();
//    }
//
//    @Benchmark
//    public void fillCacheTest() {
//        fillCache();
//    }
//
//}
//
//
