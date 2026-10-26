package bo.quispe.edwin.domain.shared.constants;

public final class PermissionsMatrix {

    private PermissionsMatrix() {}
    // SUB MODULO USERS
    public static final class USERS {
        private USERS() {}

        public static final class VER {
            private VER() {}
            public static final String NAME = "user.ver";
            public static final String TITLE = "Ver user";
            public static final String DESCRIPTION = "Detallar un usuario";
            public static final String MODULE = "Users";
        }

        public static final class LISTAR {
            private LISTAR() {}
            public static final String NAME = "user.listar";
            public static final String TITLE = "Listar usuarios";
            public static final String DESCRIPTION = "Listado de usuarios";
            public static final String MODULE = "Users";
        }

        public static final class CREAR {
            private CREAR() {}
            public static final String NAME = "user.crear";
            public static final String TITLE = "Crear usuario";
            public static final String DESCRIPTION = "Registrar nuevo usuario";
            public static final String MODULE = "Users";
        }

        public static final class EDITAR {
            private EDITAR() {}
            public static final String NAME = "user.editar";
            public static final String TITLE = "Editar usuario";
            public static final String DESCRIPTION = "Modificar usuario";
            public static final String MODULE = "Users";
        }

        public static final class ACTIVAR {
            private ACTIVAR() {}
            public static final String NAME = "user.activar";
            public static final String TITLE = "Activar usuario";
            public static final String DESCRIPTION = "Activar usuario";
            public static final String MODULE = "Users";
        }

        public static final class DESACTIVAR {
            private DESACTIVAR() {}
            public static final String NAME = "user.desactivar";
            public static final String TITLE = "Desactivar usuario";
            public static final String DESCRIPTION = "Dar de baja usuario";
            public static final String MODULE = "Users";
        }
    }

    // SUBMÓDULO DE PERMISSION
    public static final class PERMISSIONS {
        private PERMISSIONS() {}

        public static final class VER {
            private VER() {}
            public static final String NAME = "permission.ver";
            public static final String TITLE = "Ver permiso";
            public static final String DESCRIPTION = "Detallar permiso";
            public static final String MODULE = "Permissions";
        }

        public static final class LISTAR {
            private LISTAR() {}
            public static final String NAME = "permission.listar";
            public static final String TITLE = "Listar permisos";
            public static final String DESCRIPTION = "Lista de registros de permiso";
            public static final String MODULE = "Permissions";
        }

        public static final class CREAR {
            private CREAR() {}
            public static final String NAME = "permission.crear";
            public static final String TITLE = "Crear permiso";
            public static final String DESCRIPTION = "Nuevo registro permiso";
            public static final String MODULE = "Permissions";
        }

        public static final class EDITAR {
            private EDITAR() {}
            public static final String NAME = "permission.editar";
            public static final String TITLE = "Editar permiso";
            public static final String DESCRIPTION = "Editar permiso";
            public static final String MODULE = "Permissions";
        }
    }

    // SUBMÓDULO DE ROLES
    public static final class ROLES {
        private ROLES() {}

        public static final class VER {
            private VER() {}
            public static final String NAME = "role.ver";
            public static final String TITLE = "Ver role";
            public static final String DESCRIPTION = "Detallar role";
            public static final String MODULE = "Roles";
        }

        public static final class LISTAR {
            private LISTAR() {}
            public static final String NAME = "role.listar";
            public static final String TITLE = "Listar roles";
            public static final String DESCRIPTION = "Lista de registros de roles";
            public static final String MODULE = "Roles";
        }

        public static final class CREAR {
            private CREAR() {}
            public static final String NAME = "permission.role";
            public static final String TITLE = "Crear role";
            public static final String DESCRIPTION = "Nuevo registro role";
            public static final String MODULE = "Roles";
        }

        public static final class EDITAR {
            private EDITAR() {}
            public static final String NAME = "role.editar";
            public static final String TITLE = "Editar role";
            public static final String DESCRIPTION = "Editar role";
            public static final String MODULE = "Roles";
        }
    }
}
