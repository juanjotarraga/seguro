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

import org.seguro.model.Tasa;

import java.util.Iterator;

import org.seguro.model.Compania;
import org.seguro.model.DetalleTasaCondicion;
import org.seguro.model.Poliza;
import org.seguro.model.Tipo;

/**
 * Backing bean for Tasa entities.
 * <p/>
 * This class provides CRUD functionality for all Tasa entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TasaBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Tasa entities
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

   private Tasa tasa;

   public Tasa getTasa()
   {
      return this.tasa;
   }

   public void setTasa(Tasa tasa)
   {
      this.tasa = tasa;
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
         this.tasa = this.example;
      }
      else
      {
         this.tasa = findById(getId());
      }
   }

   public Tasa findById(Integer id)
   {

      return this.entityManager.find(Tasa.class, id);
   }

   /*
    * Support updating and deleting Tasa entities
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
        	 this.tasa.setFechaReg(new Date());
             this.tasa.setUsuarioReg(nombre);
         	//this.tasa.setFlagEstado("AC");
            this.entityManager.persist(this.tasa);
            return "search?faces-redirect=true";
         }
         else
         {
        	 this.tasa.setFechaMod(new Date());
        	 this.tasa.setUsuarioMod(nombre);
        	 //this.tasa.setFlagEstado("AC");
            this.entityManager.merge(this.tasa);
            return "view?faces-redirect=true&id=" + this.tasa.getIdTasa();
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
      { /*
         Tasa deletableEntity = findById(getId());
         Compania compania = deletableEntity.getCompania();
         compania.getTasas().remove(deletableEntity);
         deletableEntity.setCompania(null);
         this.entityManager.merge(compania);
         Tipo tipo = deletableEntity.getTipo();
         tipo.getTasas().remove(deletableEntity);
         deletableEntity.setTipo(null);
         this.entityManager.merge(tipo);
         Iterator<Poliza> iterPolizas = deletableEntity.getPolizas().iterator();
         for (; iterPolizas.hasNext();)
         {
            Poliza nextInPolizas = iterPolizas.next();
            nextInPolizas.setTasa(null);
            iterPolizas.remove();
            this.entityManager.merge(nextInPolizas);
         }
         Iterator<DetalleTasaCondicion> iterDetalleTasaCondicions = deletableEntity.getDetalleTasaCondicions().iterator();
         for (; iterDetalleTasaCondicions.hasNext();)
         {
            DetalleTasaCondicion nextInDetalleTasaCondicions = iterDetalleTasaCondicions.next();
            nextInDetalleTasaCondicions.setTasa(null);
            iterDetalleTasaCondicions.remove();
            this.entityManager.merge(nextInDetalleTasaCondicions);
         }
         this.entityManager.remove(deletableEntity);*/
    	  String nombre = request.getUserPrincipal().getName();
    	  Tasa deletableEntity = findById(getId());    	 
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
    * Support searching Tasa entities with pagination
    */

   private int page;
   private long count;
   private List<Tasa> pageItems;

   private Tasa example = new Tasa();

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

   public Tasa getExample()
   {
      return this.example;
   }

   public void setExample(Tasa example)
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
      Root<Tasa> root = countCriteria.from(Tasa.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Tasa> criteria = builder.createQuery(Tasa.class);
      root = criteria.from(Tasa.class);
      TypedQuery<Tasa> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Tasa> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idTasa = this.example.getIdTasa();
      if (idTasa != 0)
      {
         predicatesList.add(builder.equal(root.get("idTasa"), idTasa));
      }
      Compania compania = this.example.getCompania();
      if (compania != null)
      {
         predicatesList.add(builder.equal(root.get("compania"), compania));
      }
      Tipo tipo = this.example.getTipo();
      if (tipo != null)
      {
         predicatesList.add(builder.equal(root.get("tipo"), tipo));
      }
      Integer mayorQue = this.example.getMayorQue();
      if (mayorQue != null && mayorQue.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("mayorQue"), mayorQue));
      }
      Integer menorQue = this.example.getMenorQue();
      if (menorQue != null && menorQue.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("menorQue"), menorQue));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Tasa> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Tasa entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Tasa> getAll()
   {

      CriteriaQuery<Tasa> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Tasa.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Tasa.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TasaBean ejbProxy = this.sessionContext.getBusinessObject(TasaBean.class);

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

            return String.valueOf(((Tasa) value).getIdTasa());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Tasa add = new Tasa();

   public Tasa getAdd()
   {
      return this.add;
   }

   public Tasa getAdded()
   {
      Tasa added = this.add;
      this.add = new Tasa();
      return added;
   }
}
