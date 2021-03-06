package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Cliente generated by hbm2java
 */
@Entity
@Table(name = "cliente"
      , schema = "public")
public class Cliente implements java.io.Serializable
{

   private int idCliente;
   private String nombres;
   private String apellidos;
   private String direccion;
   private Integer telefono;
   private Integer celular;
   private String email;
   private String estado;
   private Integer nit;
   private Integer carnet;
   private String actividad;
   private String profesion;
   private Date fechaNacimiento;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<Poliza> polizas = new HashSet<Poliza>(0);

   public Cliente()
   {
   }

   public Cliente(int idCliente)
   {
      this.idCliente = idCliente;
   }

   public Cliente(int idCliente, String nombres, String apellidos, String direccion, Integer telefono, Integer celular, String email, String estado, Integer nit, Integer carnet, String actividad, String profesion, Date fechaNacimiento, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<Poliza> polizas)
   {
      this.idCliente = idCliente;
      this.nombres = nombres;
      this.apellidos = apellidos;
      this.direccion = direccion;
      this.telefono = telefono;
      this.celular = celular;
      this.email = email;
      this.estado = estado;
      this.nit = nit;
      this.carnet = carnet;
      this.actividad = actividad;
      this.profesion = profesion;
      this.fechaNacimiento = fechaNacimiento;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.polizas = polizas;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_cliente", unique = true, nullable = false, insertable=false)
   public int getIdCliente()
   {
      return this.idCliente;
   }

   public void setIdCliente(int idCliente)
   {
      this.idCliente = idCliente;
   }

   @Column(name = "nombres", length = 50)
   public String getNombres()
   {
      return this.nombres;
   }

   public void setNombres(String nombres)
   {
      this.nombres = nombres;
   }

   @Column(name = "apellidos", length = 50)
   public String getApellidos()
   {
      return this.apellidos;
   }

   public void setApellidos(String apellidos)
   {
      this.apellidos = apellidos;
   }

   @Column(name = "direccion", length = 200)
   public String getDireccion()
   {
      return this.direccion;
   }

   public void setDireccion(String direccion)
   {
      this.direccion = direccion;
   }

   @Column(name = "telefono")
   public Integer getTelefono()
   {
      return this.telefono;
   }

   public void setTelefono(Integer telefono)
   {
      this.telefono = telefono;
   }

   @Column(name = "celular")
   public Integer getCelular()
   {
      return this.celular;
   }

   public void setCelular(Integer celular)
   {
      this.celular = celular;
   }

   @Column(name = "email", length = 80)
   public String getEmail()
   {
      return this.email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   @Column(name = "estado", length = 10)
   public String getEstado()
   {
      return this.estado;
   }

   public void setEstado(String estado)
   {
      this.estado = estado;
   }

   @Column(name = "nit")
   public Integer getNit()
   {
      return this.nit;
   }

   public void setNit(Integer nit)
   {
      this.nit = nit;
   }

   @Column(name = "carnet")
   public Integer getCarnet()
   {
      return this.carnet;
   }

   public void setCarnet(Integer carnet)
   {
      this.carnet = carnet;
   }

   @Column(name = "actividad", length = 50)
   public String getActividad()
   {
      return this.actividad;
   }

   public void setActividad(String actividad)
   {
      this.actividad = actividad;
   }

   @Column(name = "profesion", length = 50)
   public String getProfesion()
   {
      return this.profesion;
   }

   public void setProfesion(String profesion)
   {
      this.profesion = profesion;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_nacimiento", length = 29)
   public Date getFechaNacimiento()
   {
      return this.fechaNacimiento;
   }

   public void setFechaNacimiento(Date fechaNacimiento)
   {
      this.fechaNacimiento = fechaNacimiento;
   }

   @Column(name = "flag_estado", length = 2)
   public String getFlagEstado()
   {
      return this.flagEstado;
   }

   public void setFlagEstado(String flagEstado)
   {
      this.flagEstado = flagEstado;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_reg", length = 29)
   public Date getFechaReg()
   {
      return this.fechaReg;
   }

   public void setFechaReg(Date fechaReg)
   {
      this.fechaReg = fechaReg;
   }

   @Column(name = "usuario_reg", length = 30)
   public String getUsuarioReg()
   {
      return this.usuarioReg;
   }

   public void setUsuarioReg(String usuarioReg)
   {
      this.usuarioReg = usuarioReg;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_mod", length = 29)
   public Date getFechaMod()
   {
      return this.fechaMod;
   }

   public void setFechaMod(Date fechaMod)
   {
      this.fechaMod = fechaMod;
   }

   @Column(name = "usuario_mod", length = 30)
   public String getUsuarioMod()
   {
      return this.usuarioMod;
   }

   public void setUsuarioMod(String usuarioMod)
   {
      this.usuarioMod = usuarioMod;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_borrado", length = 29)
   public Date getFechaBorrado()
   {
      return this.fechaBorrado;
   }

   public void setFechaBorrado(Date fechaBorrado)
   {
      this.fechaBorrado = fechaBorrado;
   }

   @Column(name = "usuario_borrado", length = 30)
   public String getUsuarioBorrado()
   {
      return this.usuarioBorrado;
   }

   public void setUsuarioBorrado(String usuarioBorrado)
   {
      this.usuarioBorrado = usuarioBorrado;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
   public Set<Poliza> getPolizas()
   {
      return this.polizas;
   }

   public void setPolizas(Set<Poliza> polizas)
   {
      this.polizas = polizas;
   }

}
