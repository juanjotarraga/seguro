package org.seguro.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
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
import javax.servlet.http.HttpServletRequest;

import org.seguro.model.Modelo;

import java.util.Iterator;

import org.seguro.model.Automotor;
import org.seguro.model.Marca;

/**
 * Backing bean for Modelo entities.
 * <p/>
 * This class provides CRUD functionality for all Modelo entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ModeloBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Modelo entities
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

   private Modelo modelo;

   public Modelo getModelo()
   {
      return this.modelo;
   }

   public void setModelo(Modelo modelo)
   {
      this.modelo = modelo;
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
         this.modelo = this.example;
      }
      else
      {
         this.modelo = findById(getId());
      }
   }

   public Modelo findById(Integer id)
   {

      return this.entityManager.find(Modelo.class, id);
   }

   /*
    * Support updating and deleting Modelo entities
    */

   public String update()
   {
	   System.out.println("Agarrando el contexto.");
       String nombre = request.getUserPrincipal().getName();
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            //this.entityManager.persist(this.modelo);
        	 this.modelo.setFechaReg(new Date());
             this.modelo.setUsuarioReg(nombre);
         	this.modelo.setFlagEstado("AC");
             this.entityManager.persist(this.modelo);
            return "search?faces-redirect=true";
         }
         else
         {
            //this.entityManager.merge(this.modelo);
        	 this.modelo.setFechaMod(new Date());
        	 this.modelo.setUsuarioMod(nombre);
        	 this.modelo.setFlagEstado("AC");
             this.entityManager.persist(this.modelo);
            return "view?faces-redirect=true&id=" + this.modelo.getIdModelo();
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
    	  /*
         Modelo deletableEntity = findById(getId());
         Marca marca = deletableEntity.getMarca();
         marca.getModelos().remove(deletableEntity);
         deletableEntity.setMarca(null);
         this.entityManager.merge(marca);
         Iterator<Automotor> iterAutomotors = deletableEntity.getAutomotors().iterator();
         for (; iterAutomotors.hasNext();)
         {
            Automotor nextInAutomotors = iterAutomotors.next();
            nextInAutomotors.setModelo(null);
            iterAutomotors.remove();
            this.entityManager.merge(nextInAutomotors);
         }
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
         */
    	  String nombre = request.getUserPrincipal().getName();
    	  Modelo deletableEntity = findById(getId());
    	  deletableEntity.setFlagEstado("IN");
    	  deletableEntity.setUsuarioBorrado(nombre);
    	  deletableEntity.setFechaBorrado(new Date());
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
    * Support searching Modelo entities with pagination
    */

   private int page;
   private long count;
   private List<Modelo> pageItems;

   private Modelo example = new Modelo();

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

   public Modelo getExample()
   {
      return this.example;
   }

   public void setExample(Modelo example)
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
      Root<Modelo> root = countCriteria.from(Modelo.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Modelo> criteria = builder.createQuery(Modelo.class);
      root = criteria.from(Modelo.class);
      TypedQuery<Modelo> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Modelo> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idModelo = this.example.getIdModelo();
      if (idModelo != 0)
      {
         predicatesList.add(builder.equal(root.get("idModelo"), idModelo));
      }
      Marca marca = this.example.getMarca();
      if (marca != null)
      {
         predicatesList.add(builder.equal(root.get("marca"), marca));
      }
      String nombre = this.example.getNombre();
      if (nombre != null && !"".equals(nombre))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
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

   public List<Modelo> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Modelo entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Modelo> getAll()
   {

      CriteriaQuery<Modelo> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Modelo.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Modelo.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ModeloBean ejbProxy = this.sessionContext.getBusinessObject(ModeloBean.class);

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

            return String.valueOf(((Modelo) value).getIdModelo());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Modelo add = new Modelo();

   public Modelo getAdd()
   {
      return this.add;
   }

   public Modelo getAdded()
   {
      Modelo added = this.add;
      this.add = new Modelo();
      return added;
   }
}
