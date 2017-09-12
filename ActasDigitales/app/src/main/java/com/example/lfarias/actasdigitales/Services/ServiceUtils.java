package com.example.lfarias.actasdigitales.Services;

/**
 * Created by acer on 04/09/2017.
 */

public class ServiceUtils {

    public static class Actions {
        /**
         * First set of constants will be the whole actions set defined in the service layer.
         */
        public static final String BUSCAR_CIUDADANO = "buscar_ciudadano_por_id_dni_mobile";
        public static final String BUSCAR_PROVINCIAS = "index_mobile";
        public static final String BUSCAR_DEPARTAMENTO_SEGUN_PROVINCIA = "Depto_segun_provincia_mobile";
        public static final String BUSCAR_LOCALIDAD_SEGUN_DEPARTAMENTO = "localidad_segun_dpto_mobile";
    }

    public static class Controllers{
        /**
         * Second set of constants will be the whole controller set deefined in the service layer.
         */
        public static final String TRAMITE_DNI_CONTROLLER = "tramitedni";
        public static final String PROVINCIA_CONTROLLER = "provincia";
        public static final String DEPARTAMENTO_CONTROLLER = "departamento";
        public static final String LOCALIDAD_CONTROLLER = "localidad";
    }

    public static class SearchType {
        /**
         * Third constant that will define the type of search so the system can handle only one asyncTask and do
         * all the different work that is needed.
         */

        public static final Integer CIUDADANO_SEARCH_TYPE = 0;
        public static final Integer PROVINCIA_SEARCH_TYPE = 1;
        public static final Integer DEPARTAMENTO_SEARCH_TYPE = 2;
        public static final Integer LOCALIDAD_SEARCH_TYPE = 3;
    }
}
