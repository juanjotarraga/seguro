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

import org.seguro.model.Compania;
import java.util.Iterator;
import org.seguro.model.Prima;
import org.seguro.model.Tasa;

/**
 * Backing bean for Compania entities.
 * <p/>
 * This class provides CRUD functionality for all Compania entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CompaniaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Compania entities
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

   private Compania compania;

   public Compania getCompania()
   {
      return this.compania;
   }

   public void setCompania(Compania compania)
   {
      this.compania = compania;
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
         this.compania = this.example;
      }
      else
      {
         this.compania = findById(getId());
      }
   }

   public Compania findById(Integer id)
   {

      return this.entityManager.find(Compania.class, id);
   }

   /*
    * Support updating and deleting Compania entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.compania);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.compania);
            return "view?faces-redirect=true&id=" + this.compania.getIdCompania();
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
         Compania deletableEntity = findById(getId());
         Iterator<Prima> iterPrimas = deletableEntity.getPrimas().iterator();
         for (; iterPrimas.hasNext();)
         {
            Prima nextInPrimas = iterPrimas.next();
            nextInPrimas.setCompania(null);
            iterPrimas.remove();
            this.entityManager.merge(nextInPrimas);
         }
         Iterator<Tasa> iterTasas = deletableEntity.getTasas().iterator();
         for (; iterTasas.hasNext();)
         {
            Tasa nextInTasas = iterTasas.next();
            nextInTasas.setCompania(null);
            iterTasas.remove();
            this.entityManager.merge(nextInTasas);
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
    * Support searching Compania entities with pagination
    */

   private int page;
   private long count;
   private List<Compania> pageItems;

   private Compania example = new Compania();

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

   public Compania getExample()
   {
      return this.example;
   }

   public void setExample(Compania example)
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
      Root<Compania> root = countCriteria.from(Compania.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Compania> criteria = builder.createQuery(Compania.class);
      root = criteria.from(Compania.class);
      TypedQuery<Compania> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Compania> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCompania = this.example.getIdCompania();
      if (idCompania != 0)
      {
         predicatesList.add(builder.equal(root.get("idCompania"), idCompania));
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
      String logo = this.example.getLogo();
      if (logo != null && !"".equals(logo))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("logo")), '%' + logo.toLowerCase() + '%'));
      }
      String mail = this.example.getMail();
      if (mail != null && !"".equals(mail))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("mail")), '%' + mail.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Compania> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Compania entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Compania> getAll()
   {

      CriteriaQuery<Compania> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Compania.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Compania.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CompaniaBean ejbProxy = this.sessionContext.getBusinessObject(CompaniaBean.class);

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

            return String.valueOf(((Compania) value).getIdCompania());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Compania add = new Compania();

   public Compania getAdd()
   {
      return this.add;
   }

   public Compania getAdded()
   {
      Compania added = this.add;
      this.add = new Compania();
      return added;
   }
}
