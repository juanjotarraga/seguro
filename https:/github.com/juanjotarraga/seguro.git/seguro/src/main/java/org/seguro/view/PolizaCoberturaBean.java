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

import org.seguro.model.PolizaCobertura;
import org.seguro.model.Poliza;

/**
 * Backing bean for PolizaCobertura entities.
 * <p/>
 * This class provides CRUD functionality for all PolizaCobertura entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PolizaCoberturaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving PolizaCobertura entities
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

   private PolizaCobertura polizaCobertura;

   public PolizaCobertura getPolizaCobertura()
   {
      return this.polizaCobertura;
   }

   public void setPolizaCobertura(PolizaCobertura polizaCobertura)
   {
      this.polizaCobertura = polizaCobertura;
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
         this.polizaCobertura = this.example;
      }
      else
      {
         this.polizaCobertura = findById(getId());
      }
   }

   public PolizaCobertura findById(Integer id)
   {

      return this.entityManager.find(PolizaCobertura.class, id);
   }

   /*
    * Support updating and deleting PolizaCobertura entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.polizaCobertura);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.polizaCobertura);
            return "view?faces-redirect=true&id=" + this.polizaCobertura.getIdCobertura();
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
         PolizaCobertura deletableEntity = findById(getId());
         Poliza poliza = deletableEntity.getPoliza();
         poliza.getPolizaCoberturas().remove(deletableEntity);
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
    * Support searching PolizaCobertura entities with pagination
    */

   private int page;
   private long count;
   private List<PolizaCobertura> pageItems;

   private PolizaCobertura example = new PolizaCobertura();

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

   public PolizaCobertura getExample()
   {
      return this.example;
   }

   public void setExample(PolizaCobertura example)
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
      Root<PolizaCobertura> root = countCriteria.from(PolizaCobertura.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<PolizaCobertura> criteria = builder.createQuery(PolizaCobertura.class);
      root = criteria.from(PolizaCobertura.class);
      TypedQuery<PolizaCobertura> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<PolizaCobertura> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCobertura = this.example.getIdCobertura();
      if (idCobertura != 0)
      {
         predicatesList.add(builder.equal(root.get("idCobertura"), idCobertura));
      }
      Poliza poliza = this.example.getPoliza();
      if (poliza != null)
      {
         predicatesList.add(builder.equal(root.get("poliza"), poliza));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
      }
      String acronimo = this.example.getAcronimo();
      if (acronimo != null && !"".equals(acronimo))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("acronimo")), '%' + acronimo.toLowerCase() + '%'));
      }
      String flagEstado = this.example.getFlagEstado();
      if (flagEstado != null && !"".equals(flagEstado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("flagEstado")), '%' + flagEstado.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<PolizaCobertura> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back PolizaCobertura entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<PolizaCobertura> getAll()
   {

      CriteriaQuery<PolizaCobertura> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(PolizaCobertura.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(PolizaCobertura.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PolizaCoberturaBean ejbProxy = this.sessionContext.getBusinessObject(PolizaCoberturaBean.class);

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

            return String.valueOf(((PolizaCobertura) value).getIdCobertura());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private PolizaCobertura add = new PolizaCobertura();

   public PolizaCobertura getAdd()
   {
      return this.add;
   }

   public PolizaCobertura getAdded()
   {
      PolizaCobertura added = this.add;
      this.add = new PolizaCobertura();
      return added;
   }
}
