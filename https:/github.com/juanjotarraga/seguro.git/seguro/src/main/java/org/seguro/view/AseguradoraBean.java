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

import org.seguro.model.Aseguradora;
import java.util.Iterator;
import org.seguro.model.Usuario;

/**
 * Backing bean for Aseguradora entities.
 * <p/>
 * This class provides CRUD functionality for all Aseguradora entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AseguradoraBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Aseguradora entities
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

   private Aseguradora aseguradora;

   public Aseguradora getAseguradora()
   {
      return this.aseguradora;
   }

   public void setAseguradora(Aseguradora aseguradora)
   {
      this.aseguradora = aseguradora;
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
         this.aseguradora = this.example;
      }
      else
      {
         this.aseguradora = findById(getId());
      }
   }

   public Aseguradora findById(Integer id)
   {

      return this.entityManager.find(Aseguradora.class, id);
   }

   /*
    * Support updating and deleting Aseguradora entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.aseguradora);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.aseguradora);
            return "view?faces-redirect=true&id=" + this.aseguradora.getIdAseguradora();
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
         Aseguradora deletableEntity = findById(getId());
         Iterator<Usuario> iterUsuarios = deletableEntity.getUsuarios().iterator();
         for (; iterUsuarios.hasNext();)
         {
            Usuario nextInUsuarios = iterUsuarios.next();
            nextInUsuarios.setAseguradora(null);
            iterUsuarios.remove();
            this.entityManager.merge(nextInUsuarios);
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
    * Support searching Aseguradora entities with pagination
    */

   private int page;
   private long count;
   private List<Aseguradora> pageItems;

   private Aseguradora example = new Aseguradora();

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

   public Aseguradora getExample()
   {
      return this.example;
   }

   public void setExample(Aseguradora example)
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
      Root<Aseguradora> root = countCriteria.from(Aseguradora.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Aseguradora> criteria = builder.createQuery(Aseguradora.class);
      root = criteria.from(Aseguradora.class);
      TypedQuery<Aseguradora> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Aseguradora> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idAseguradora = this.example.getIdAseguradora();
      if (idAseguradora != 0)
      {
         predicatesList.add(builder.equal(root.get("idAseguradora"), idAseguradora));
      }
      String nombreCorto = this.example.getNombreCorto();
      if (nombreCorto != null && !"".equals(nombreCorto))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombreCorto")), '%' + nombreCorto.toLowerCase() + '%'));
      }
      String nombreLargo = this.example.getNombreLargo();
      if (nombreLargo != null && !"".equals(nombreLargo))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombreLargo")), '%' + nombreLargo.toLowerCase() + '%'));
      }
      String logo = this.example.getLogo();
      if (logo != null && !"".equals(logo))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("logo")), '%' + logo.toLowerCase() + '%'));
      }
      String emailResponsable = this.example.getEmailResponsable();
      if (emailResponsable != null && !"".equals(emailResponsable))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("emailResponsable")), '%' + emailResponsable.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Aseguradora> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Aseguradora entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Aseguradora> getAll()
   {

      CriteriaQuery<Aseguradora> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Aseguradora.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Aseguradora.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final AseguradoraBean ejbProxy = this.sessionContext.getBusinessObject(AseguradoraBean.class);

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

            return String.valueOf(((Aseguradora) value).getIdAseguradora());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Aseguradora add = new Aseguradora();

   public Aseguradora getAdd()
   {
      return this.add;
   }

   public Aseguradora getAdded()
   {
      Aseguradora added = this.add;
      this.add = new Aseguradora();
      return added;
   }
}
