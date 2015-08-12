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

import org.seguro.model.Tipo;

import java.util.Iterator;

import org.seguro.model.Automotor;
import org.seguro.model.Tasa;

/**
 * Backing bean for Tipo entities.
 * <p/>
 * This class provides CRUD functionality for all Tipo entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TipoBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Tipo entities
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

   private Tipo tipo;

   public Tipo getTipo()
   {
      return this.tipo;
   }

   public void setTipo(Tipo tipo)
   {
      this.tipo = tipo;
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
         this.tipo = this.example;
      }
      else
      {
         this.tipo = findById(getId());
      }
   }

   public Tipo findById(Integer id)
   {

      return this.entityManager.find(Tipo.class, id);
   }

   /*
    * Support updating and deleting Tipo entities
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
        	 this.tipo.setFechaReg(new Date());
             this.tipo.setUsuarioReg(nombre);
         	this.tipo.setFlagEstado("AC");
            this.entityManager.persist(this.tipo);
            return "search?faces-redirect=true";
         }
         else
         {
        	 this.tipo.setFechaMod(new Date());
        	 this.tipo.setUsuarioMod(nombre);
        	 this.tipo.setFlagEstado("AC");
            this.entityManager.merge(this.tipo);
            return "view?faces-redirect=true&id=" + this.tipo.getIdTipo();
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
         Tipo deletableEntity = findById(getId());
         Iterator<Tasa> iterTasas = deletableEntity.getTasas().iterator();
         for (; iterTasas.hasNext();)
         {
            Tasa nextInTasas = iterTasas.next();
            nextInTasas.setTipo(null);
            iterTasas.remove();
            this.entityManager.merge(nextInTasas);
         }
         Iterator<Automotor> iterAutomotors = deletableEntity.getAutomotors().iterator();
         for (; iterAutomotors.hasNext();)
         {
            Automotor nextInAutomotors = iterAutomotors.next();
            nextInAutomotors.setTipo(null);
            iterAutomotors.remove();
            this.entityManager.merge(nextInAutomotors);
         }
         this.entityManager.remove(deletableEntity); */
    	  String nombre = request.getUserPrincipal().getName();
    	  Tipo deletableEntity = findById(getId());
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
    * Support searching Tipo entities with pagination
    */

   private int page;
   private long count;
   private List<Tipo> pageItems;

   private Tipo example = new Tipo();

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

   public Tipo getExample()
   {
      return this.example;
   }

   public void setExample(Tipo example)
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
      Root<Tipo> root = countCriteria.from(Tipo.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Tipo> criteria = builder.createQuery(Tipo.class);
      root = criteria.from(Tipo.class);
      TypedQuery<Tipo> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Tipo> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idTipo = this.example.getIdTipo();
      if (idTipo != 0)
      {
         predicatesList.add(builder.equal(root.get("idTipo"), idTipo));
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
      String usuarioMod = this.example.getUsuarioMod();
      if (usuarioMod != null && !"".equals(usuarioMod))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioMod")), '%' + usuarioMod.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Tipo> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Tipo entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Tipo> getAll()
   {

      CriteriaQuery<Tipo> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Tipo.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Tipo.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TipoBean ejbProxy = this.sessionContext.getBusinessObject(TipoBean.class);

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

            return String.valueOf(((Tipo) value).getIdTipo());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Tipo add = new Tipo();

   public Tipo getAdd()
   {
      return this.add;
   }

   public Tipo getAdded()
   {
      Tipo added = this.add;
      this.add = new Tipo();
      return added;
   }
}
