package org.seguro.model;

// Generated 11-ago-2015 18:28:13 by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PlanPago generated by hbm2java
 */
@Entity
@Table(name = "plan_pago"
      , schema = "public")
public class PlanPago implements java.io.Serializable
{

   private int idPlanPago;
   private Poliza poliza;
   private Integer cuota;
   private Date fechaPago;
   private BigDecimal importe;
   private BigDecimal primaTotal;
   private BigDecimal primaNeta;
   private BigDecimal comision;
   private String flagEstado;
   private Date fechaReg;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;

   public PlanPago()
   {
   }

   public PlanPago(int idPlanPago)
   {
      this.idPlanPago = idPlanPago;
   }

   public PlanPago(int idPlanPago, Poliza poliza, Integer cuota, Date fechaPago, BigDecimal importe, BigDecimal primaTotal, BigDecimal primaNeta, BigDecimal comision, String flagEstado, Date fechaReg, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado)
   {
      this.idPlanPago = idPlanPago;
      this.poliza = poliza;
      this.cuota = cuota;
      this.fechaPago = fechaPago;
      this.importe = importe;
      this.primaTotal = primaTotal;
      this.primaNeta = primaNeta;
      this.comision = comision;
      this.flagEstado = flagEstado;
      this.fechaReg = fechaReg;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
   }

   @Id
   @Column(name = "id_plan_pago", unique = true, nullable = false)
   public int getIdPlanPago()
   {
      return this.idPlanPago;
   }

   public void setIdPlanPago(int idPlanPago)
   {
      this.idPlanPago = idPlanPago;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_poliza")
   public Poliza getPoliza()
   {
      return this.poliza;
   }

   public void setPoliza(Poliza poliza)
   {
      this.poliza = poliza;
   }

   @Column(name = "cuota")
   public Integer getCuota()
   {
      return this.cuota;
   }

   public void setCuota(Integer cuota)
   {
      this.cuota = cuota;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_pago", length = 29)
   public Date getFechaPago()
   {
      return this.fechaPago;
   }

   public void setFechaPago(Date fechaPago)
   {
      this.fechaPago = fechaPago;
   }

   @Column(name = "importe", precision = 18)
   public BigDecimal getImporte()
   {
      return this.importe;
   }

   public void setImporte(BigDecimal importe)
   {
      this.importe = importe;
   }

   @Column(name = "prima_total", precision = 18)
   public BigDecimal getPrimaTotal()
   {
      return this.primaTotal;
   }

   public void setPrimaTotal(BigDecimal primaTotal)
   {
      this.primaTotal = primaTotal;
   }

   @Column(name = "prima_neta", precision = 18)
   public BigDecimal getPrimaNeta()
   {
      return this.primaNeta;
   }

   public void setPrimaNeta(BigDecimal primaNeta)
   {
      this.primaNeta = primaNeta;
   }

   @Column(name = "comision", precision = 18)
   public BigDecimal getComision()
   {
      return this.comision;
   }

   public void setComision(BigDecimal comision)
   {
      this.comision = comision;
   }

   @Column(name = "flag_estado", length = 20)
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

}