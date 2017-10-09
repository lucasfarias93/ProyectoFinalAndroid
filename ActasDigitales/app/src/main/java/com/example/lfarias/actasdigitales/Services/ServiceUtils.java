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
        public static final String ENVIAR_CODIGO = "correoandroid";
        public static final String VERIFICAR_CODIGO = "codigoactivacionandroid";
        public static final String REGISTER_USER = "crear_mobile";
        public static final String LOGIN_USER = "login";
        public static final String CIUDADANO_ID = "getCurrentId";
        public static final String VALIDEZ_ACTA = "verificar_validez_usuario_mobile";
        public static final String BUSCAR_IMAGEN_MOBILE = "buscar_imagen_mobile";
        public static final String CAMBIAR_CLAVE_MOBILE = "cambiar_clave_mobile";

    }

    public static class Controllers{
        /**
         * Second set of constants will be the whole controller set deefined in the service layer.
         */
        public static final String TRAMITE_DNI_CONTROLLER = "tramitedni";
        public static final String PROVINCIA_CONTROLLER = "provincia";
        public static final String DEPARTAMENTO_CONTROLLER = "departamento";
        public static final String LOCALIDAD_CONTROLLER = "localidad";
        public static final String REGISTER_USER_CONTROLLER = "usuarios";
        public static final String LOGIN_USER_CONTROLLER = "mobile/mobile";
        public static final String CIUDADANO_CONTROLLER = "ciudadano";

        public static final String RECUPERACION_CONTRASEÑA_PATH = "olvido";
        public static final String COMMON_INDEX_METHOD = "index";
        public static final String RECUPERAR_CONTRASEÑA_CAMBIO_CONTROLLER = "recuperar";
        public static final String REGISTER_USER = "crear_mobile";
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
        public static final Integer ENVIAR_CODIGO = 4;
        public static final Integer VALIDAR_CODIGO = 5;
        public static final Integer REGISTER_USER_SEARCH_TYPE = 6;
        public static final Integer LOGIN_USER_SEARCH_TYPE = 7;
        public static final Integer USER_ID_SEARCH_TYPE = 8;
        public static final Integer VALIDEZ_ACTA_SEARCH_TYPE = 9;
        public static final Integer IMAGEN_ACTA_SEARCH_TYPE = 10;
        public static final Integer CAMBIAR_CLAVE_MOBILE = 11;
    }
}
