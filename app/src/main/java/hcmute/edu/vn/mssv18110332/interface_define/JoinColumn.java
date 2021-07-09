package hcmute.edu.vn.mssv18110332.interface_define;

public @interface JoinColumn {
    String name() default "";

    String referencedColumnName() default "";

    boolean unique() default false;

    boolean nullable() default true;

    boolean insertable() default true;

    boolean updatable() default true;

    String columnDefinition() default "";

    String table() default "";

}