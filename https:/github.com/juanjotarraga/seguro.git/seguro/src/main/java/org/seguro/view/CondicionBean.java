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

import org.seguro.model.Condicion;
import java.util.Iterator;
import org.seguro.model.CoberturaSiniestro;
import org.seguro.model.DetalleTasaCondicion;

/**
 * Backing bean for Condicion entities.
 * <p/>
 * This class provides CRUD functionality for all Condicion entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CondicionBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Condicion entities
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

   private Condicion condicion;

   public Condicion getCondicion()
   {
      return this.condicion;
   }

   public void setCondicion(Condicion condicion)
   {
      this.condicion = condicion;
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
         this.condicion = this.example;
      }
      else
      {
         this.condicion = findById(getId());
      }
   }

   public Condicion findById(Integer id)
   {

      return this.entityManager.find(Condicion.class, id);
   }

   /*
    * Support updating and deleting Condicion entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.condicion);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.condicion);
            return "view?faces-redirect=true&id=" + this.condicion.getIdCondicion();
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
         Condicion deletableEntity = findById(getId());
         Iterator<DetalleTasaCondicion> iterDetalleTasaCondicions = deletableEntity.getDetalleTasaCondicions().iterator();
         for (; iterDetalleTasaCondicions.hasNext();)
         {
            DetalleTasaCondicion nextInDetalleTasaCondicions = iterDetalleTasaCondicions.next();
            nextInDetalleTasaCondicions.setCondicion(null);
            iterDetalleTasaCondicions.remove();
            this.entityManager.merge(nextInDetalleTasaCondicions);
         }
         Iterator<CoberturaSiniestro> iterCoberturaSiniestros = deletableEntity.getCoberturaSiniestros().iterator();
         for (; iterCoberturaSiniestros.hasNext();)
         {
            CoberturaSiniestro nextInCoberturaSiniestros = iterCoberturaSiniestros.next();
            nextInCoberturaSiniestros.setCondicion(null);
            iterCoberturaSiniestros.remove();
            this.entityManager.merge(nextInCoberturaSiniestros);
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
    * Support searching Condicion entities with pagination
    */

   private int page;
   private long count;
   private List<Condicion> pageItems;

   private Condicion example = new Condicion();

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

   public Condicion getExample()
   {
      return this.example;
   }

   public void setExample(Condicion example)
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
      Root<Condicion> root = countCriteria.from(Condicion.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Condicion> criteria = builder.createQuery(Condicion.class);
      root = criteria.from(Condicion.class);
      TypedQuery<Condicion> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Condicion> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCondicion = this.example.getIdCondicion();
      if (idCondicion != 0)
      {
         predicatesList.add(builder.equal(root.get("idCondicion"), idCondicion));
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
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Condicion> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Condicion entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Condicion> getAll()
   {

      CriteriaQuery<Condicion> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Condicion.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Condicion.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CondicionBean ejbProxy = this.sessionContext.getBusinessObject(CondicionBean.class);

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

            return String.valueOf(((Condicion) value).getIdCondicion());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Condicion add = new Condicion();

   public Condicion getAdd()
   {
      return this.add;
   }

   public Condicion getAdded()
   {
      Condicion added = this.add;
      this.add = new Condicion();
      return added;
   }
}
