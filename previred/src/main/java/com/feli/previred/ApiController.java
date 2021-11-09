package com.feli.previred;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/")

public class ApiController {

    /**Método GET - http://localhost:8081/getFechas
     * A través del archivo en la ruta src/main/resources/request.json
     * se mapea la data de este para obtener las fechas faltantes en los rangos dados.
    */
    @GetMapping(path = "/getFechas", produces=MediaType.APPLICATION_JSON_VALUE)
    public Periodo getFechasFaltantes(){
        String json = getJSONPeriodo();
        Periodo periodo =  new Periodo();   
        periodo = formatearJSONPeriodo(json);
        return periodo;
    }

    /**Método POST - http://localhost:8081/getFechas
     * Se ingresa a través de una consulta POST un body con estructura JSON, con el cual
     * se mapea la data de este para obtener las fechas faltantes en los rangos dados.
    */
    @PostMapping(path ="/getFechas", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Periodo> postFechasFaltantes(@RequestBody String json){
        Periodo periodo =  new Periodo();   
        periodo = formatearJSONPeriodo(json);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
          .path("/")
          .buildAndExpand(periodo.getId())
          .toUri();
 
        return ResponseEntity.created(uri)
          .body(periodo);
    }

    /**
     * Se instancia el objeto Periodo a través de una cadena de caracteres
     * el cual será usado como Objeto JSON para el response.
    */
    public Periodo formatearJSONPeriodo(String json){
        try {
            List<String> fechas = new ArrayList<>();
            JSONObject jsonPeriodo = new JSONObject(json);
            int id = jsonPeriodo.getInt("id");
            String fechaCreacion = jsonPeriodo.getString("fechaCreacion");
            String fechaFin = jsonPeriodo.getString("fechaFin");
            JSONArray fechasJson = jsonPeriodo.getJSONArray("fechas");

            for (int index = 0; index < fechasJson.length(); index++) {
                String fecha = fechasJson.getString(index);
                fechas.add(fecha);
            }
            return nuevoPeriodo(id, fechaCreacion, fechaFin, fechas);
        } catch (Exception e) {
            e.printStackTrace();
            return new Periodo();
        }
    }

    /**
     * Se obtienen las fechas faltantes del objeto Periodo
     * el cual será usado como Objeto JSON para el response.
    */
    public Periodo formatearJSON(Periodo periodo){
        try {
            int id = periodo.getId();
            String fechaCreacion = periodo.getFechaCreacion();
            String fechaFin = periodo.getFechaFin();
            List<String> fechas = periodo.getFechas();
            return nuevoPeriodo(id, fechaCreacion, fechaFin, fechas);
        } catch (Exception e) {
            e.printStackTrace();
            return new Periodo();
        }
    }

    /**
     * Se instancia el objeto Periodo
     * el cual será usado como Objeto JSON para el response.
    */
    public Periodo nuevoPeriodo (int id, String fechaCreacion, String fechaFin, List<String> fechas){
        Periodo periodo = new Periodo();
        try {
            periodo.setId(id);
            periodo.setFechaCreacion(fechaCreacion);
            periodo.setFechaFin(fechaFin);
            periodo.setFechas(fechas);
            periodo.setFechasFaltantes(getFechasFaltantes(fechas, fechaCreacion, fechaFin));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return periodo;
    }

    /**
     * Se obtienen las fechas faltantes de la siguiente manera:
     * 1) Se obtienen los años de inicio y fin para establecer un ciclo.
     * 2) Se obtienen los meses de inicio y fin para establecer desde donde comenzar y terminar.
     * 3) Se compara la fecha actual con la lista de fechas. 
    */
    public List<String> getFechasFaltantes(List<String> fechas, String fechaCreacion, String fechaFinal){
        List<String> fechasFaltantes = new ArrayList<>();
        try {
            String[] fechaIni = fechaCreacion.split("-");
            String[] fechaFin = fechaFinal.split("-");
            int anioIni =   Integer.parseInt(fechaIni[0]);
            int mesIni =    Integer.parseInt(fechaIni[1]);
            int anioFin =   Integer.parseInt(fechaFin[0]);
            int mesFin =    Integer.parseInt(fechaFin[1]);
            int anioPosicion = anioIni;

            while(anioPosicion <= anioFin){          
                
                    int mesActual = anioIni == anioPosicion ? mesIni : 1;
                    int mesFinal = anioFin == anioPosicion ? mesFin : 12; 
                    for (int j = mesActual; j <= mesFinal; j++) {
                        String mes = j >= 10 ? ""+ j : "0"+j;
                        String fechaActual = anioPosicion + "-" + mes + "-01";
                        if(!fechas.contains(fechaActual)){
                            fechasFaltantes.add(fechaActual);
                        }
                    }
                anioPosicion++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fechasFaltantes;
    }

    public String getJSONPeriodo(){
        String file = "src/main/resources/request.json";
        String json = "";
        try {
            json = readFileAsString(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
    
}
