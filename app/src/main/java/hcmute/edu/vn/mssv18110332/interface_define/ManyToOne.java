package hcmute.edu.vn.mssv18110332.interface_define;

public @interface ManyToOne {
    Class targetEntity() default void.class;

    boolean optional() default true;
}