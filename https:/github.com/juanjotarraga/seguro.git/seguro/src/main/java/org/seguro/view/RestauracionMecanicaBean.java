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

import org.seguro.model.RestauracionMecanica;
import org.seguro.model.ServicioMecanico;
import org.seguro.model.Siniestro;
import org.seguro.model.TallerMecanico;

/**
 * Backing bean for RestauracionMecanica entities.
 * <p/>
 * This class provides CRUD functionality for all RestauracionMecanica entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RestauracionMecanicaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving RestauracionMecanica entities
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

   private RestauracionMecanica restauracionMecanica;

   public RestauracionMecanica getRestauracionMecanica()
   {
      return this.restauracionMecanica;
   }

   public void setRestauracionMecanica(RestauracionMecanica restauracionMecanica)
   {
      this.restauracionMecanica = restauracionMecanica;
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
         this.restauracionMecanica = this.example;
      }
      else
      {
         this.restauracionMecanica = findById(getId());
      }
   }

   public RestauracionMecanica findById(Integer id)
   {

      return this.entityManager.find(RestauracionMecanica.class, id);
   }

   /*
    * Support updating and deleting RestauracionMecanica entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.restauracionMecanica);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.restauracionMecanica);
            return "view?faces-redirect=true&id=" + this.restauracionMecanica.getIdRestauracionMecanica();
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
         RestauracionMecanica deletableEntity = findById(getId());
         ServicioMecanico servicioMecanico = deletableEntity.getServicioMecanico();
         servicioMecanico.getRestauracionMecanicas().remove(deletableEntity);
         deletableEntity.setServicioMecanico(null);
         this.entityManager.merge(servicioMecanico);
         Siniestro siniestro = deletableEntity.getSiniestro();
         siniestro.getRestauracionMecanicas().remove(deletableEntity);
         deletableEntity.setSiniestro(null);
         this.entityManager.merge(siniestro);
         TallerMecanico tallerMecanico = deletableEntity.getTallerMecanico();
         tallerMecanico.getRestauracionMecanicas().remove(deletableEntity);
         deletableEntity.setTallerMecanico(null);
         this.entityManager.merge(tallerMecanico);
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
    * Support searching RestauracionMecanica entities with pagination
    */

   private int page;
   private long count;
   private List<RestauracionMecanica> pageItems;

   private RestauracionMecanica example = new RestauracionMecanica();

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

   public RestauracionMecanica getExample()
   {
      return this.example;
   }

   public void setExample(RestauracionMecanica example)
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
      Root<RestauracionMecanica> root = countCriteria.from(RestauracionMecanica.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<RestauracionMecanica> criteria = builder.createQuery(RestauracionMecanica.class);
      root = criteria.from(RestauracionMecanica.class);
      TypedQuery<RestauracionMecanica> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<RestauracionMecanica> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idRestauracionMecanica = this.example.getIdRestauracionMecanica();
      if (idRestauracionMecanica != 0)
      {
         predicatesList.add(builder.equal(root.get("idRestauracionMecanica"), idRestauracionMecanica));
      }
      ServicioMecanico servicioMecanico = this.example.getServicioMecanico();
      if (servicioMecanico != null)
      {
         predicatesList.add(builder.equal(root.get("servicioMecanico"), servicioMecanico));
      }
      Siniestro siniestro = this.example.getSiniestro();
      if (siniestro != null)
      {
         predicatesList.add(builder.equal(root.get("siniestro"), siniestro));
      }
      TallerMecanico tallerMecanico = this.example.getTallerMecanico();
      if (tallerMecanico != null)
      {
         predicatesList.add(builder.equal(root.get("tallerMecanico"), tallerMecanico));
      }
      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<RestauracionMecanica> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back RestauracionMecanica entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<RestauracionMecanica> getAll()
   {

      CriteriaQuery<RestauracionMecanica> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(RestauracionMecanica.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(RestauracionMecanica.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final RestauracionMecanicaBean ejbProxy = this.sessionContext.getBusinessObject(RestauracionMecanicaBean.class);

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

            return String.valueOf(((RestauracionMecanica) value).getIdRestauracionMecanica());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private RestauracionMecanica add = new RestauracionMecanica();

   public RestauracionMecanica getAdd()
   {
      return this.add;
   }

   public RestauracionMecanica getAdded()
   {
      RestauracionMecanica added = this.add;
      this.add = new RestauracionMecanica();
      return added;
   }
}
