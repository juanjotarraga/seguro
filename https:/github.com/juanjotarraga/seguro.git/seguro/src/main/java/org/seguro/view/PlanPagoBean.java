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

import org.seguro.model.PlanPago;
import org.seguro.model.Poliza;

/**
 * Backing bean for PlanPago entities.
 * <p/>
 * This class provides CRUD functionality for all PlanPago entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanPagoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving PlanPago entities
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

   private PlanPago planPago;

   public PlanPago getPlanPago()
   {
      return this.planPago;
   }

   public void setPlanPago(PlanPago planPago)
   {
      this.planPago = planPago;
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
         this.planPago = this.example;
      }
      else
      {
         this.planPago = findById(getId());
      }
   }

   public PlanPago findById(Integer id)
   {

      return this.entityManager.find(PlanPago.class, id);
   }

   /*
    * Support updating and deleting PlanPago entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.planPago);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.planPago);
            return "view?faces-redirect=true&id=" + this.planPago.getIdPlanPago();
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
         PlanPago deletableEntity = findById(getId());
         Poliza poliza = deletableEntity.getPoliza();
         poliza.getPlanPagos().remove(deletableEntity);
         deletableEntity.setPoliza(null);
         this.entityManager.merge(poliza);
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
    * Support searching PlanPago entities with pagination
    */

   private int page;
   private long count;
   private List<PlanPago> pageItems;

   private PlanPago example = new PlanPago();

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

   public PlanPago getExample()
   {
      return this.example;
   }

   public void setExample(PlanPago example)
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
      Root<PlanPago> root = countCriteria.from(PlanPago.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<PlanPago> criteria = builder.createQuery(PlanPago.class);
      root = criteria.from(PlanPago.class);
      TypedQuery<PlanPago> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<PlanPago> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idPlanPago = this.example.getIdPlanPago();
      if (idPlanPago != 0)
      {
         predicatesList.add(builder.equal(root.get("idPlanPago"), idPlanPago));
      }
      Poliza poliza = this.example.getPoliza();
      if (poliza != null)
      {
         predicatesList.add(builder.equal(root.get("poliza"), poliza));
      }
      Integer cuota = this.example.getCuota();
      if (cuota != null && cuota.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("cuota"), cuota));
      }
      String flagEstado = this.example.getFlagEstado();
      if (flagEstado != null && !"".equals(flagEstado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("flagEstado")), '%' + flagEstado.toLowerCase() + '%'));
      }
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<PlanPago> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back PlanPago entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<PlanPago> getAll()
   {

      CriteriaQuery<PlanPago> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(PlanPago.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(PlanPago.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PlanPagoBean ejbProxy = this.sessionContext.getBusinessObject(PlanPagoBean.class);

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

            return String.valueOf(((PlanPago) value).getIdPlanPago());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private PlanPago add = new PlanPago();

   public PlanPago getAdd()
   {
      return this.add;
   }

   public PlanPago getAdded()
   {
      PlanPago added = this.add;
      this.add = new PlanPago();
      return added;
   }
}
