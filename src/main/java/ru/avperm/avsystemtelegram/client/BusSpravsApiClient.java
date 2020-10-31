package ru.avperm.avsystemtelegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.avperm.avsystemtelegram.config.BusSpravsApiClientConfiguration;
import ru.avperm.avsystemtelegram.dto.BusStation;
import ru.avperm.avsystemtelegram.dto.Order;
import ru.avperm.avsystemtelegram.dto.Race;
import ru.avperm.avsystemtelegram.dto.Stopplace;

import java.util.List;

@FeignClient(value = "busSpravClient", url = "${application.linkspravapi}", configuration = BusSpravsApiClientConfiguration.class)
public interface BusSpravsApiClient {

    @GetMapping("/busstation/all")
    List<BusStation> getAllBusStation();

    @GetMapping("/busstation/{id}")
    BusStation getBusStationById(@PathVariable("id") String idBusStation);

    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable("id") String idOrder);

    @GetMapping("/sp/all")
    List<Stopplace> getAllStopplaces();

    @GetMapping("/sp/{id}")
    Stopplace getStopplaceById(@PathVariable("id") String idStopplace);

    @GetMapping("/race/all")
    List<Race> getAllRaces();

    @GetMapping("/race/today")
    List<Race> getAllRacesToday();

    @GetMapping("/race/{idFrom}/{idTo}")
    List<Race> getRacesBySpFromSpTo(@PathVariable("idFrom") String idFrom, @PathVariable("idTo") String idTo);

    @GetMapping("/race/{id}")
    Race getRaceById(@PathVariable("id") String idRace);

}
