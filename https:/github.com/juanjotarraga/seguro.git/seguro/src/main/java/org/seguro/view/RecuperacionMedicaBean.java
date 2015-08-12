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

import org.seguro.model.RecuperacionMedica;
import org.seguro.model.CentroSalud;
import org.seguro.model.ServicioSalud;
import org.seguro.model.Siniestro;

/**
 * Backing bean for RecuperacionMedica entities.
 * <p/>
 * This class provides CRUD functionality for all RecuperacionMedica entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RecuperacionMedicaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving RecuperacionMedica entities
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

   private RecuperacionMedica recuperacionMedica;

   public RecuperacionMedica getRecuperacionMedica()
   {
      return this.recuperacionMedica;
   }

   public void setRecuperacionMedica(RecuperacionMedica recuperacionMedica)
   {
      this.recuperacionMedica = recuperacionMedica;
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
         this.recuperacionMedica = this.example;
      }
      else
      {
         this.recuperacionMedica = findById(getId());
      }
   }

   public RecuperacionMedica findById(Integer id)
   {

      return this.entityManager.find(RecuperacionMedica.class, id);
   }

   /*
    * Support updating and deleting RecuperacionMedica entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.recuperacionMedica);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.recuperacionMedica);
            return "view?faces-redirect=true&id=" + this.recuperacionMedica.getIdRecuperacionMedica();
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
         RecuperacionMedica deletableEntity = findById(getId());
         CentroSalud centroSalud = deletableEntity.getCentroSalud();
         centroSalud.getRecuperacionMedicas().remove(deletableEntity);
         deletableEntity.setCentroSalud(null);
         this.entityManager.merge(centroSalud);
         ServicioSalud servicioSalud = deletableEntity.getServicioSalud();
         servicioSalud.getRecuperacionMedicas().remove(deletableEntity);
         deletableEntity.setServicioSalud(null);
         this.entityManager.merge(servicioSalud);
         Siniestro siniestro = deletableEntity.getSiniestro();
         siniestro.getRecuperacionMedicas().remove(deletableEntity);
         deletableEntity.setSiniestro(null);
         this.entityManager.merge(siniestro);
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
    * Support searching RecuperacionMedica entities with pagination
    */

   private int page;
   private long count;
   private List<RecuperacionMedica> pageItems;

   private RecuperacionMedica example = new RecuperacionMedica();

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

   public RecuperacionMedica getExample()
   {
      return this.example;
   }

   public void setExample(RecuperacionMedica example)
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
      Root<RecuperacionMedica> root = countCriteria.from(RecuperacionMedica.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<RecuperacionMedica> criteria = builder.createQuery(RecuperacionMedica.class);
      root = criteria.from(RecuperacionMedica.class);
      TypedQuery<RecuperacionMedica> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<RecuperacionMedica> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idRecuperacionMedica = this.example.getIdRecuperacionMedica();
      if (idRecuperacionMedica != 0)
      {
         predicatesList.add(builder.equal(root.get("idRecuperacionMedica"), idRecuperacionMedica));
      }
      CentroSalud centroSalud = this.example.getCentroSalud();
      if (centroSalud != null)
      {
         predicatesList.add(builder.equal(root.get("centroSalud"), centroSalud));
      }
      ServicioSalud servicioSalud = this.example.getServicioSalud();
      if (servicioSalud != null)
      {
         predicatesList.add(builder.equal(root.get("servicioSalud"), servicioSalud));
      }
      Siniestro siniestro = this.example.getSiniestro();
      if (siniestro != null)
      {
         predicatesList.add(builder.equal(root.get("siniestro"), siniestro));
      }
      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<RecuperacionMedica> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back RecuperacionMedica entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<RecuperacionMedica> getAll()
   {

      CriteriaQuery<RecuperacionMedica> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(RecuperacionMedica.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(RecuperacionMedica.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final RecuperacionMedicaBean ejbProxy = this.sessionContext.getBusinessObject(RecuperacionMedicaBean.class);

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

            return String.valueOf(((RecuperacionMedica) value).getIdRecuperacionMedica());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private RecuperacionMedica add = new RecuperacionMedica();

   public RecuperacionMedica getAdd()
   {
      return this.add;
   }

   public RecuperacionMedica getAdded()
   {
      RecuperacionMedica added = this.add;
      this.add = new RecuperacionMedica();
      return added;
   }
}
