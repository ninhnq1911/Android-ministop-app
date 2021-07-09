package hcmute.edu.vn.mssv18110332.interface_define;

public @interface OneToMany {
    Class targetEntity() default void.class;

    String mappedBy() default "";

    boolean orphanRemoval() default false;
}