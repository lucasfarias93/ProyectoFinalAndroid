package com.example.lfarias.actasdigitales.Services;

/**
 * Creado por Lucas.Farias
 *
 * Archivo creado: 3 de Septiembre de 2017
 *
 * Descripción: Utiles utilizados para el armado de la conexión con todos los webservice.
 *
 * @params  Actions: definen el nombre del método o acción en PHP el cual cumple el rol de webservice (encargado de
 *                   una acción en particular, y solo una)
 *
 *          Controllers: nombre de los controllers donde estan ubicadas las actions. Un controller usualmente esta formado
 *                       por, como mínimo, 2 acciones (una compatible con el cliente web, y otra paridad para la aplicación andriod)
 *
 *          SearchType: valores numericos utilizados en caso de conexiones de una sola entrada con múltiples resultados,
 *                      de forma de discriminar cada caso, y devolver un valor especifico a ese caso, no otro.
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
        public static final String BUSCAR_PARENTESCO = "buscar_parentesco_tipolibro_mobile";
        public static final String LISTADO_SOLICITUDES = "listado_android";
        public static final String CREAR_SOLICITUD = "crear_solicitud_android";
        public static final String BUSCAR_DATOS_MOBILE = "buscar_datos_mobile";

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
        public static final String LISTADO_PATH = "listado";
        public static final String SOLICITUD_PATH = "solicitud";
        public static final String RECUPERAR_CONTRASEÑA_CAMBIO_CONTROLLER = "recuperar";
        public static final String REGISTER_USER = "crear_mobile";
        public static final String VERIFICAR_CONTROLLER = "verificar";
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
        public static final Integer BUSCAR_PARENTESCO_SEARCH_TYPE = 12;
        public static final Integer BUSCAR_LISTADO_SOLICITUDES_TYPE = 13;
        public static final Integer CREAR_SOLICITUD_SEARCH_TYPE = 14;
        public static final Integer OBTENER_DATOS_SEARCH_TYPE = 15;
    }

    public static class RequestData{

        public static final int nacimiento = 1;
        public static final int matrimonio = 3;
        public static final int defuncion = 2;
        public static final int union = 9;


        public static final int propia = 1;
        public static final int padre = 3;
        public static final int madre = 2;
        public static final int conyuge = 6;
        public static final int hijos = 18;

    }
}
