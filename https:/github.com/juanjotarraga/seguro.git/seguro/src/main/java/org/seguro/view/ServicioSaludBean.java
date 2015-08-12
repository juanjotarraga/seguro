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

import org.seguro.model.ServicioSalud;
import java.util.Iterator;
import org.seguro.model.RecuperacionMedica;

/**
 * Backing bean for ServicioSalud entities.
 * <p/>
 * This class provides CRUD functionality for all ServicioSalud entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ServicioSaludBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving ServicioSalud entities
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

   private ServicioSalud servicioSalud;

   public ServicioSalud getServicioSalud()
   {
      return this.servicioSalud;
   }

   public void setServicioSalud(ServicioSalud servicioSalud)
   {
      this.servicioSalud = servicioSalud;
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
         this.servicioSalud = this.example;
      }
      else
      {
         this.servicioSalud = findById(getId());
      }
   }

   public ServicioSalud findById(Integer id)
   {

      return this.entityManager.find(ServicioSalud.class, id);
   }

   /*
    * Support updating and deleting ServicioSalud entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.servicioSalud);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.servicioSalud);
            return "view?faces-redirect=true&id=" + this.servicioSalud.getIdServicioSalud();
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
         ServicioSalud deletableEntity = findById(getId());
         Iterator<RecuperacionMedica> iterRecuperacionMedicas = deletableEntity.getRecuperacionMedicas().iterator();
         for (; iterRecuperacionMedicas.hasNext();)
         {
            RecuperacionMedica nextInRecuperacionMedicas = iterRecuperacionMedicas.next();
            nextInRecuperacionMedicas.setServicioSalud(null);
            iterRecuperacionMedicas.remove();
            this.entityManager.merge(nextInRecuperacionMedicas);
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
    * Support searching ServicioSalud entities with pagination
    */

   private int page;
   private long count;
   private List<ServicioSalud> pageItems;

   private ServicioSalud example = new ServicioSalud();

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

   public ServicioSalud getExample()
   {
      return this.example;
   }

   public void setExample(ServicioSalud example)
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
      Root<ServicioSalud> root = countCriteria.from(ServicioSalud.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<ServicioSalud> criteria = builder.createQuery(ServicioSalud.class);
      root = criteria.from(ServicioSalud.class);
      TypedQuery<ServicioSalud> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<ServicioSalud> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idServicioSalud = this.example.getIdServicioSalud();
      if (idServicioSalud != 0)
      {
         predicatesList.add(builder.equal(root.get("idServicioSalud"), idServicioSalud));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
      }
      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
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

   public List<ServicioSalud> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back ServicioSalud entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<ServicioSalud> getAll()
   {

      CriteriaQuery<ServicioSalud> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(ServicioSalud.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(ServicioSalud.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ServicioSaludBean ejbProxy = this.sessionContext.getBusinessObject(ServicioSaludBean.class);

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

            return String.valueOf(((ServicioSalud) value).getIdServicioSalud());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private ServicioSalud add = new ServicioSalud();

   public ServicioSalud getAdd()
   {
      return this.add;
   }

   public ServicioSalud getAdded()
   {
      ServicioSalud added = this.add;
      this.add = new ServicioSalud();
      return added;
   }
}
