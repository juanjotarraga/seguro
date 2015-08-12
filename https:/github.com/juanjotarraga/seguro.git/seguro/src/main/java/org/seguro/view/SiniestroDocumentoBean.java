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

import org.seguro.model.SiniestroDocumento;
import org.seguro.model.Siniestro;

/**
 * Backing bean for SiniestroDocumento entities.
 * <p/>
 * This class provides CRUD functionality for all SiniestroDocumento entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SiniestroDocumentoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving SiniestroDocumento entities
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

   private SiniestroDocumento siniestroDocumento;

   public SiniestroDocumento getSiniestroDocumento()
   {
      return this.siniestroDocumento;
   }

   public void setSiniestroDocumento(SiniestroDocumento siniestroDocumento)
   {
      this.siniestroDocumento = siniestroDocumento;
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
         this.siniestroDocumento = this.example;
      }
      else
      {
         this.siniestroDocumento = findById(getId());
      }
   }

   public SiniestroDocumento findById(Integer id)
   {

      return this.entityManager.find(SiniestroDocumento.class, id);
   }

   /*
    * Support updating and deleting SiniestroDocumento entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.siniestroDocumento);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.siniestroDocumento);
            return "view?faces-redirect=true&id=" + this.siniestroDocumento.getIdPsiniestroDocumento();
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
         SiniestroDocumento deletableEntity = findById(getId());
         Siniestro siniestro = deletableEntity.getSiniestro();
         siniestro.getSiniestroDocumentos().remove(deletableEntity);
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
    * Support searching SiniestroDocumento entities with pagination
    */

   private int page;
   private long count;
   private List<SiniestroDocumento> pageItems;

   private SiniestroDocumento example = new SiniestroDocumento();

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

   public SiniestroDocumento getExample()
   {
      return this.example;
   }

   public void setExample(SiniestroDocumento example)
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
      Root<SiniestroDocumento> root = countCriteria.from(SiniestroDocumento.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<SiniestroDocumento> criteria = builder.createQuery(SiniestroDocumento.class);
      root = criteria.from(SiniestroDocumento.class);
      TypedQuery<SiniestroDocumento> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<SiniestroDocumento> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idPsiniestroDocumento = this.example.getIdPsiniestroDocumento();
      if (idPsiniestroDocumento != 0)
      {
         predicatesList.add(builder.equal(root.get("idPsiniestroDocumento"), idPsiniestroDocumento));
      }
      Siniestro siniestro = this.example.getSiniestro();
      if (siniestro != null)
      {
         predicatesList.add(builder.equal(root.get("siniestro"), siniestro));
      }
      String carnet = this.example.getCarnet();
      if (carnet != null && !"".equals(carnet))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("carnet")), '%' + carnet.toLowerCase() + '%'));
      }
      String rua = this.example.getRua();
      if (rua != null && !"".equals(rua))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("rua")), '%' + rua.toLowerCase() + '%'));
      }
      String otros = this.example.getOtros();
      if (otros != null && !"".equals(otros))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("otros")), '%' + otros.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<SiniestroDocumento> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back SiniestroDocumento entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SiniestroDocumento> getAll()
   {

      CriteriaQuery<SiniestroDocumento> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SiniestroDocumento.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(SiniestroDocumento.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SiniestroDocumentoBean ejbProxy = this.sessionContext.getBusinessObject(SiniestroDocumentoBean.class);

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

            return String.valueOf(((SiniestroDocumento) value).getIdPsiniestroDocumento());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private SiniestroDocumento add = new SiniestroDocumento();

   public SiniestroDocumento getAdd()
   {
      return this.add;
   }

   public SiniestroDocumento getAdded()
   {
      SiniestroDocumento added = this.add;
      this.add = new SiniestroDocumento();
      return added;
   }
}
