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

import org.seguro.model.Prima;
import org.seguro.model.Compania;
import org.seguro.model.Poliza;

/**
 * Backing bean for Prima entities.
 * <p/>
 * This class provides CRUD functionality for all Prima entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PrimaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Prima entities
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

   private Prima prima;

   public Prima getPrima()
   {
      return this.prima;
   }

   public void setPrima(Prima prima)
   {
      this.prima = prima;
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
         this.prima = this.example;
      }
      else
      {
         this.prima = findById(getId());
      }
   }

   public Prima findById(Integer id)
   {

      return this.entityManager.find(Prima.class, id);
   }

   /*
    * Support updating and deleting Prima entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.prima);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.prima);
            return "view?faces-redirect=true&id=" + this.prima.getIdPrima();
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
         Prima deletableEntity = findById(getId());
         Compania compania = deletableEntity.getCompania();
         compania.getPrimas().remove(deletableEntity);
         deletableEntity.setCompania(null);
         this.entityManager.merge(compania);
         Poliza poliza = deletableEntity.getPoliza();
         poliza.getPrimas().remove(deletableEntity);
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
    * Support searching Prima entities with pagination
    */

   private int page;
   private long count;
   private List<Prima> pageItems;

   private Prima example = new Prima();

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

   public Prima getExample()
   {
      return this.example;
   }

   public void setExample(Prima example)
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
      Root<Prima> root = countCriteria.from(Prima.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Prima> criteria = builder.createQuery(Prima.class);
      root = criteria.from(Prima.class);
      TypedQuery<Prima> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Prima> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idPrima = this.example.getIdPrima();
      if (idPrima != 0)
      {
         predicatesList.add(builder.equal(root.get("idPrima"), idPrima));
      }
      Compania compania = this.example.getCompania();
      if (compania != null)
      {
         predicatesList.add(builder.equal(root.get("compania"), compania));
      }
      Poliza poliza = this.example.getPoliza();
      if (poliza != null)
      {
         predicatesList.add(builder.equal(root.get("poliza"), poliza));
      }
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }
      String usuarioMod = this.example.getUsuarioMod();
      if (usuarioMod != null && !"".equals(usuarioMod))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioMod")), '%' + usuarioMod.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Prima> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Prima entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Prima> getAll()
   {

      CriteriaQuery<Prima> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Prima.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Prima.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PrimaBean ejbProxy = this.sessionContext.getBusinessObject(PrimaBean.class);

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

            return String.valueOf(((Prima) value).getIdPrima());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Prima add = new Prima();

   public Prima getAdd()
   {
      return this.add;
   }

   public Prima getAdded()
   {
      Prima added = this.add;
      this.add = new Prima();
      return added;
   }
}
