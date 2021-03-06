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

import org.seguro.model.Automotor;

import java.util.Iterator;

import org.seguro.model.Cliente;
import org.seguro.model.Modelo;
import org.seguro.model.Poliza;
import org.seguro.model.Tipo;

/**
 * Backing bean for Automotor entities.
 * <p/>
 * This class provides CRUD functionality for all Automotor entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AutomotorBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   FacesContext context = FacesContext.getCurrentInstance();
   ExternalContext externalContext = context.getExternalContext();
   HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

   /*
    * Support creating and retrieving Automotor entities
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

   private Automotor automotor;

   public Automotor getAutomotor()
   {
      return this.automotor;
   }

   public void setAutomotor(Automotor automotor)
   {
      this.automotor = automotor;
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
         this.automotor = this.example;
      }
      else
      {
         this.automotor = findById(getId());
      }
   }

   public Automotor findById(Integer id)
   {

      return this.entityManager.find(Automotor.class, id);
   }

   /*
    * Support updating and deleting Automotor entities
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
        	 this.automotor.setFechaReg(new Date());
             this.automotor.setUsuarioReg(nombre);
         	this.automotor.setFlagEstado("AC");
            this.entityManager.persist(this.automotor);
            return "search?faces-redirect=true";
         }
         else
         {
        	 this.automotor.setFechaMod(new Date());
        	 this.automotor.setUsuarioMod(nombre);
        	 this.automotor.setFlagEstado("AC");
            this.entityManager.merge(this.automotor);
            return "view?faces-redirect=true&id=" + this.automotor.getIdAutomotor();
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
         Automotor deletableEntity = findById(getId());
         Modelo modelo = deletableEntity.getModelo();
         modelo.getAutomotors().remove(deletableEntity);
         deletableEntity.setModelo(null);
         this.entityManager.merge(modelo);
         Tipo tipo = deletableEntity.getTipo();
         tipo.getAutomotors().remove(deletableEntity);
         deletableEntity.setTipo(null);
         this.entityManager.merge(tipo);
         Iterator<Poliza> iterPolizas = deletableEntity.getPolizas().iterator();
         for (; iterPolizas.hasNext();)
         {
            Poliza nextInPolizas = iterPolizas.next();
            nextInPolizas.setAutomotor(null);
            iterPolizas.remove();
            this.entityManager.merge(nextInPolizas);
         }
         this.entityManager.remove(deletableEntity);*/
    	  String nombre = request.getUserPrincipal().getName();
    	  Automotor deletableEntity = findById(getId());
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
    * Support searching Automotor entities with pagination
    */

   private int page;
   private long count;
   private List<Automotor> pageItems;

   private Automotor example = new Automotor();

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

   public Automotor getExample()
   {
      return this.example;
   }

   public void setExample(Automotor example)
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
      Root<Automotor> root = countCriteria.from(Automotor.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Automotor> criteria = builder.createQuery(Automotor.class);
      root = criteria.from(Automotor.class);
      TypedQuery<Automotor> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Automotor> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idAutomotor = this.example.getIdAutomotor();
      if (idAutomotor != 0)
      {
         predicatesList.add(builder.equal(root.get("idAutomotor"), idAutomotor));
      }
      Modelo modelo = this.example.getModelo();
      if (modelo != null)
      {
         predicatesList.add(builder.equal(root.get("modelo"), modelo));
      }
      Tipo tipo = this.example.getTipo();
      if (tipo != null)
      {
         predicatesList.add(builder.equal(root.get("tipo"), tipo));
      }
      String ano = this.example.getAno();
      if (ano != null && !"".equals(ano))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("ano")), '%' + ano.toLowerCase() + '%'));
      }
      String placa = this.example.getPlaca();
      if (placa != null && !"".equals(placa))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("placa")), '%' + placa.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Automotor> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Automotor entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Automotor> getAll()
   {

      CriteriaQuery<Automotor> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Automotor.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Automotor.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final AutomotorBean ejbProxy = this.sessionContext.getBusinessObject(AutomotorBean.class);

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

            return String.valueOf(((Automotor) value).getIdAutomotor());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Automotor add = new Automotor();

   public Automotor getAdd()
   {
      return this.add;
   }

   public Automotor getAdded()
   {
      Automotor added = this.add;
      this.add = new Automotor();
      return added;
   }
}
