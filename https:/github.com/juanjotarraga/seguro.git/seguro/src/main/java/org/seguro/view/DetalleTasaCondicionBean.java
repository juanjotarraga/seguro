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

import org.seguro.model.DetalleTasaCondicion;
import org.seguro.model.Condicion;
import org.seguro.model.Tasa;

/**
 * Backing bean for DetalleTasaCondicion entities.
 * <p/>
 * This class provides CRUD functionality for all DetalleTasaCondicion entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DetalleTasaCondicionBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving DetalleTasaCondicion entities
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

   private DetalleTasaCondicion detalleTasaCondicion;

   public DetalleTasaCondicion getDetalleTasaCondicion()
   {
      return this.detalleTasaCondicion;
   }

   public void setDetalleTasaCondicion(DetalleTasaCondicion detalleTasaCondicion)
   {
      this.detalleTasaCondicion = detalleTasaCondicion;
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
         this.detalleTasaCondicion = this.example;
      }
      else
      {
         this.detalleTasaCondicion = findById(getId());
      }
   }

   public DetalleTasaCondicion findById(Integer id)
   {

      return this.entityManager.find(DetalleTasaCondicion.class, id);
   }

   /*
    * Support updating and deleting DetalleTasaCondicion entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.detalleTasaCondicion);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.detalleTasaCondicion);
            return "view?faces-redirect=true&id=" + this.detalleTasaCondicion.getIdDetalleTasaCondicion();
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
         DetalleTasaCondicion deletableEntity = findById(getId());
         Condicion condicion = deletableEntity.getCondicion();
         condicion.getDetalleTasaCondicions().remove(deletableEntity);
         deletableEntity.setCondicion(null);
         this.entityManager.merge(condicion);
         Tasa tasa = deletableEntity.getTasa();
         tasa.getDetalleTasaCondicions().remove(deletableEntity);
         deletableEntity.setTasa(null);
         this.entityManager.merge(tasa);
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
    * Support searching DetalleTasaCondicion entities with pagination
    */

   private int page;
   private long count;
   private List<DetalleTasaCondicion> pageItems;

   private DetalleTasaCondicion example = new DetalleTasaCondicion();

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

   public DetalleTasaCondicion getExample()
   {
      return this.example;
   }

   public void setExample(DetalleTasaCondicion example)
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
      Root<DetalleTasaCondicion> root = countCriteria.from(DetalleTasaCondicion.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<DetalleTasaCondicion> criteria = builder.createQuery(DetalleTasaCondicion.class);
      root = criteria.from(DetalleTasaCondicion.class);
      TypedQuery<DetalleTasaCondicion> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<DetalleTasaCondicion> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idDetalleTasaCondicion = this.example.getIdDetalleTasaCondicion();
      if (idDetalleTasaCondicion != 0)
      {
         predicatesList.add(builder.equal(root.get("idDetalleTasaCondicion"), idDetalleTasaCondicion));
      }
      Condicion condicion = this.example.getCondicion();
      if (condicion != null)
      {
         predicatesList.add(builder.equal(root.get("condicion"), condicion));
      }
      Tasa tasa = this.example.getTasa();
      if (tasa != null)
      {
         predicatesList.add(builder.equal(root.get("tasa"), tasa));
      }
      String observaciones = this.example.getObservaciones();
      if (observaciones != null && !"".equals(observaciones))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("observaciones")), '%' + observaciones.toLowerCase() + '%'));
      }
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<DetalleTasaCondicion> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back DetalleTasaCondicion entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<DetalleTasaCondicion> getAll()
   {

      CriteriaQuery<DetalleTasaCondicion> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(DetalleTasaCondicion.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(DetalleTasaCondicion.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final DetalleTasaCondicionBean ejbProxy = this.sessionContext.getBusinessObject(DetalleTasaCondicionBean.class);

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

            return String.valueOf(((DetalleTasaCondicion) value).getIdDetalleTasaCondicion());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private DetalleTasaCondicion add = new DetalleTasaCondicion();

   public DetalleTasaCondicion getAdd()
   {
      return this.add;
   }

   public DetalleTasaCondicion getAdded()
   {
      DetalleTasaCondicion added = this.add;
      this.add = new DetalleTasaCondicion();
      return added;
   }
}
