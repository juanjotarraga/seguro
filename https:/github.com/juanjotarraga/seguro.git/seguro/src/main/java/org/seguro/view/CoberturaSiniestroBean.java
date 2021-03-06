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

import org.seguro.model.CoberturaSiniestro;
import org.seguro.model.Condicion;
import org.seguro.model.Siniestro;

/**
 * Backing bean for CoberturaSiniestro entities.
 * <p/>
 * This class provides CRUD functionality for all CoberturaSiniestro entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CoberturaSiniestroBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving CoberturaSiniestro entities
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

   private CoberturaSiniestro coberturaSiniestro;

   public CoberturaSiniestro getCoberturaSiniestro()
   {
      return this.coberturaSiniestro;
   }

   public void setCoberturaSiniestro(CoberturaSiniestro coberturaSiniestro)
   {
      this.coberturaSiniestro = coberturaSiniestro;
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
         this.coberturaSiniestro = this.example;
      }
      else
      {
         this.coberturaSiniestro = findById(getId());
      }
   }

   public CoberturaSiniestro findById(Integer id)
   {

      return this.entityManager.find(CoberturaSiniestro.class, id);
   }

   /*
    * Support updating and deleting CoberturaSiniestro entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.coberturaSiniestro);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.coberturaSiniestro);
            return "view?faces-redirect=true&id=" + this.coberturaSiniestro.getIdCoberturaSiniestro();
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
         CoberturaSiniestro deletableEntity = findById(getId());
         Condicion condicion = deletableEntity.getCondicion();
         condicion.getCoberturaSiniestros().remove(deletableEntity);
         deletableEntity.setCondicion(null);
         this.entityManager.merge(condicion);
         Siniestro siniestro = deletableEntity.getSiniestro();
         siniestro.getCoberturaSiniestros().remove(deletableEntity);
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
    * Support searching CoberturaSiniestro entities with pagination
    */

   private int page;
   private long count;
   private List<CoberturaSiniestro> pageItems;

   private CoberturaSiniestro example = new CoberturaSiniestro();

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

   public CoberturaSiniestro getExample()
   {
      return this.example;
   }

   public void setExample(CoberturaSiniestro example)
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
      Root<CoberturaSiniestro> root = countCriteria.from(CoberturaSiniestro.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<CoberturaSiniestro> criteria = builder.createQuery(CoberturaSiniestro.class);
      root = criteria.from(CoberturaSiniestro.class);
      TypedQuery<CoberturaSiniestro> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<CoberturaSiniestro> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCoberturaSiniestro = this.example.getIdCoberturaSiniestro();
      if (idCoberturaSiniestro != 0)
      {
         predicatesList.add(builder.equal(root.get("idCoberturaSiniestro"), idCoberturaSiniestro));
      }
      Condicion condicion = this.example.getCondicion();
      if (condicion != null)
      {
         predicatesList.add(builder.equal(root.get("condicion"), condicion));
      }
      Siniestro siniestro = this.example.getSiniestro();
      if (siniestro != null)
      {
         predicatesList.add(builder.equal(root.get("siniestro"), siniestro));
      }
      String documentos = this.example.getDocumentos();
      if (documentos != null && !"".equals(documentos))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("documentos")), '%' + documentos.toLowerCase() + '%'));
      }
      String usuarioReg = this.example.getUsuarioReg();
      if (usuarioReg != null && !"".equals(usuarioReg))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("usuarioReg")), '%' + usuarioReg.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<CoberturaSiniestro> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back CoberturaSiniestro entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<CoberturaSiniestro> getAll()
   {

      CriteriaQuery<CoberturaSiniestro> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(CoberturaSiniestro.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(CoberturaSiniestro.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CoberturaSiniestroBean ejbProxy = this.sessionContext.getBusinessObject(CoberturaSiniestroBean.class);

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

            return String.valueOf(((CoberturaSiniestro) value).getIdCoberturaSiniestro());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private CoberturaSiniestro add = new CoberturaSiniestro();

   public CoberturaSiniestro getAdd()
   {
      return this.add;
   }

   public CoberturaSiniestro getAdded()
   {
      CoberturaSiniestro added = this.add;
      this.add = new CoberturaSiniestro();
      return added;
   }
}
