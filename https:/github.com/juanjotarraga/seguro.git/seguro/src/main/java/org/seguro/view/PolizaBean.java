package org.seguro.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.seguro.model.Poliza;
import java.util.Iterator;
import org.seguro.model.Automotor;
import org.seguro.model.Cliente;
import org.seguro.model.PlanPago;
import org.seguro.model.PolizaCobertura;
import org.seguro.model.PolizaDocumento;
import org.seguro.model.PolizaFoto;
import org.seguro.model.Prima;
import org.seguro.model.Siniestro;
import org.seguro.model.Tasa;

/**
 * Backing bean for Poliza entities.
 * <p/>
 * This class provides CRUD functionality for all Poliza entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PolizaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Poliza entities
    */

   private Integer id;

   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   private Poliza poliza;

   public Poliza getPoliza()
   {
      return this.poliza;
   }

   public void setPoliza(Poliza poliza)
   {
      this.poliza = poliza;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "seguro-persistence-unit", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.poliza = this.example;
      }
      else
      {
         this.poliza = findById(getId());
      }
   }

   public Poliza findById(Integer id)
   {

      return this.entityManager.find(Poliza.class, id);
   }

   /*
    * Support updating and deleting Poliza entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.poliza);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.poliza);
            return "view?faces-redirect=true&id=" + this.poliza.getIdPoliza();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         Poliza deletableEntity = findById(getId());
         Automotor automotor = deletableEntity.getAutomotor();
         automotor.getPolizas().remove(deletableEntity);
         deletableEntity.setAutomotor(null);
         this.entityManager.merge(automotor);
         Cliente cliente = deletableEntity.getCliente();
         cliente.getPolizas().remove(deletableEntity);
         deletableEntity.setCliente(null);
         this.entityManager.merge(cliente);
         Tasa tasa = deletableEntity.getTasa();
         tasa.getPolizas().remove(deletableEntity);
         deletableEntity.setTasa(null);
         this.entityManager.merge(tasa);
         Iterator<PlanPago> iterPlanPagos = deletableEntity.getPlanPagos().iterator();
         for (; iterPlanPagos.hasNext();)
         {
            PlanPago nextInPlanPagos = iterPlanPagos.next();
            nextInPlanPagos.setPoliza(null);
            iterPlanPagos.remove();
            this.entityManager.merge(nextInPlanPagos);
         }
         Iterator<PolizaCobertura> iterPolizaCoberturas = deletableEntity.getPolizaCoberturas().iterator();
         for (; iterPolizaCoberturas.hasNext();)
         {
            PolizaCobertura nextInPolizaCoberturas = iterPolizaCoberturas.next();
            nextInPolizaCoberturas.setPoliza(null);
            iterPolizaCoberturas.remove();
            this.entityManager.merge(nextInPolizaCoberturas);
         }
         Iterator<Prima> iterPrimas = deletableEntity.getPrimas().iterator();
         for (; iterPrimas.hasNext();)
         {
            Prima nextInPrimas = iterPrimas.next();
            nextInPrimas.setPoliza(null);
            iterPrimas.remove();
            this.entityManager.merge(nextInPrimas);
         }
         Iterator<PolizaDocumento> iterPolizaDocumentos = deletableEntity.getPolizaDocumentos().iterator();
         for (; iterPolizaDocumentos.hasNext();)
         {
            PolizaDocumento nextInPolizaDocumentos = iterPolizaDocumentos.next();
            nextInPolizaDocumentos.setPoliza(null);
            iterPolizaDocumentos.remove();
            this.entityManager.merge(nextInPolizaDocumentos);
         }
         Iterator<Siniestro> iterSiniestros = deletableEntity.getSiniestros().iterator();
         for (; iterSiniestros.hasNext();)
         {
            Siniestro nextInSiniestros = iterSiniestros.next();
            nextInSiniestros.setPoliza(null);
            iterSiniestros.remove();
            this.entityManager.merge(nextInSiniestros);
         }
         Iterator<PolizaFoto> iterPolizaFotos = deletableEntity.getPolizaFotos().iterator();
         for (; iterPolizaFotos.hasNext();)
         {
            PolizaFoto nextInPolizaFotos = iterPolizaFotos.next();
            nextInPolizaFotos.setPoliza(null);
            iterPolizaFotos.remove();
            this.entityManager.merge(nextInPolizaFotos);
         }
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Poliza entities with pagination
    */

   private int page;
   private long count;
   private List<Poliza> pageItems;

   private Poliza example = new Poliza();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Poliza getExample()
   {
      return this.example;
   }

   public void setExample(Poliza example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Poliza> root = countCriteria.from(Poliza.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Poliza> criteria = builder.createQuery(Poliza.class);
      root = criteria.from(Poliza.class);
      TypedQuery<Poliza> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Poliza> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idPoliza = this.example.getIdPoliza();
      if (idPoliza != 0)
      {
         predicatesList.add(builder.equal(root.get("idPoliza"), idPoliza));
      }
      Automotor automotor = this.example.getAutomotor();
      if (automotor != null)
      {
         predicatesList.add(builder.equal(root.get("automotor"), automotor));
      }
      Cliente cliente = this.example.getCliente();
      if (cliente != null)
      {
         predicatesList.add(builder.equal(root.get("cliente"), cliente));
      }
      Tasa tasa = this.example.getTasa();
      if (tasa != null)
      {
         predicatesList.add(builder.equal(root.get("tasa"), tasa));
      }
      Integer nroCotizacion = this.example.getNroCotizacion();
      if (nroCotizacion != null && nroCotizacion.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("nroCotizacion"), nroCotizacion));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Poliza> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Poliza entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Poliza> getAll()
   {

      CriteriaQuery<Poliza> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Poliza.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Poliza.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PolizaBean ejbProxy = this.sessionContext.getBusinessObject(PolizaBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Integer.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Poliza) value).getIdPoliza());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Poliza add = new Poliza();

   public Poliza getAdd()
   {
      return this.add;
   }

   public Poliza getAdded()
   {
      Poliza added = this.add;
      this.add = new Poliza();
      return added;
   }
}
