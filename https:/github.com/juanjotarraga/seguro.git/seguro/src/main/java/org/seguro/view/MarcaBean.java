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

import org.seguro.model.Marca;

import java.util.Iterator;

import org.seguro.model.Modelo;

/**
 * Backing bean for Marca entities.
 * <p/>
 * This class provides CRUD functionality for all Marca entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MarcaBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Marca entities
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

   private Marca marca;

   public Marca getMarca()
   {
      return this.marca;
   }

   public void setMarca(Marca marca)
   {
      this.marca = marca;
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
         this.marca = this.example;
      }
      else
      {
         this.marca = findById(getId());
      }
   }

   public Marca findById(Integer id)
   {

      return this.entityManager.find(Marca.class, id);
   }

   /*
    * Support updating and deleting Marca entities
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
            //this.entityManager.persist(this.marca);
            
            this.marca.setFechaReg(new Date());
            this.marca.setUsuarioReg(nombre);
        	this.marca.setFlagEstado("AC");
            this.entityManager.persist(this.marca);
            return "search?faces-redirect=true";
         }
         else
         {
            //this.entityManager.merge(this.marca);
        	 this.marca.setFechaMod(new Date());
        	 this.marca.setUsuarioMod(nombre);
        	 this.marca.setFlagEstado("AC");
             this.entityManager.persist(this.marca);
            return "view?faces-redirect=true&id=" + this.marca.getIdMarca();
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
         Marca deletableEntity = findById(getId());
         Iterator<Modelo> iterModelos = deletableEntity.getModelos().iterator();
         for (; iterModelos.hasNext();)
         {
            Modelo nextInModelos = iterModelos.next();
            nextInModelos.setMarca(null);
            iterModelos.remove();
            this.entityManager.merge(nextInModelos);
         }
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
         */
    	  String nombre = request.getUserPrincipal().getName();
    	  Marca deletableEntity = findById(getId());
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
    * Support searching Marca entities with pagination
    */

   private int page;
   private long count;
   private List<Marca> pageItems;

   private Marca example = new Marca();

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

   public Marca getExample()
   {
      return this.example;
   }

   public void setExample(Marca example)
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
      Root<Marca> root = countCriteria.from(Marca.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Marca> criteria = builder.createQuery(Marca.class);
      root = criteria.from(Marca.class);
      TypedQuery<Marca> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Marca> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idMarca = this.example.getIdMarca();
      if (idMarca != 0)
      {
         predicatesList.add(builder.equal(root.get("idMarca"), idMarca));
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

   public List<Marca> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Marca entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Marca> getAll()
   {

      CriteriaQuery<Marca> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Marca.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Marca.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final MarcaBean ejbProxy = this.sessionContext.getBusinessObject(MarcaBean.class);

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

            return String.valueOf(((Marca) value).getIdMarca());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Marca add = new Marca();

   public Marca getAdd()
   {
      return this.add;
   }

   public Marca getAdded()
   {
      Marca added = this.add;
      this.add = new Marca();
      return added;
   }
}
