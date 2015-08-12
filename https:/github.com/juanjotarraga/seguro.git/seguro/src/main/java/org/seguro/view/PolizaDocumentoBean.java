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

import org.seguro.model.PolizaDocumento;
import org.seguro.model.Poliza;

/**
 * Backing bean for PolizaDocumento entities.
 * <p/>
 * This class provides CRUD functionality for all PolizaDocumento entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PolizaDocumentoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving PolizaDocumento entities
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

   private PolizaDocumento polizaDocumento;

   public PolizaDocumento getPolizaDocumento()
   {
      return this.polizaDocumento;
   }

   public void setPolizaDocumento(PolizaDocumento polizaDocumento)
   {
      this.polizaDocumento = polizaDocumento;
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
         this.polizaDocumento = this.example;
      }
      else
      {
         this.polizaDocumento = findById(getId());
      }
   }

   public PolizaDocumento findById(Integer id)
   {

      return this.entityManager.find(PolizaDocumento.class, id);
   }

   /*
    * Support updating and deleting PolizaDocumento entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.polizaDocumento);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.polizaDocumento);
            return "view?faces-redirect=true&id=" + this.polizaDocumento.getIdPolizaDocumento();
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
         PolizaDocumento deletableEntity = findById(getId());
         Poliza poliza = deletableEntity.getPoliza();
         poliza.getPolizaDocumentos().remove(deletableEntity);
         deletableEntity.setPoliza(null);
         this.entityManager.merge(poliza);
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
    * Support searching PolizaDocumento entities with pagination
    */

   private int page;
   private long count;
   private List<PolizaDocumento> pageItems;

   private PolizaDocumento example = new PolizaDocumento();

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

   public PolizaDocumento getExample()
   {
      return this.example;
   }

   public void setExample(PolizaDocumento example)
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
      Root<PolizaDocumento> root = countCriteria.from(PolizaDocumento.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<PolizaDocumento> criteria = builder.createQuery(PolizaDocumento.class);
      root = criteria.from(PolizaDocumento.class);
      TypedQuery<PolizaDocumento> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<PolizaDocumento> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idPolizaDocumento = this.example.getIdPolizaDocumento();
      if (idPolizaDocumento != 0)
      {
         predicatesList.add(builder.equal(root.get("idPolizaDocumento"), idPolizaDocumento));
      }
      Poliza poliza = this.example.getPoliza();
      if (poliza != null)
      {
         predicatesList.add(builder.equal(root.get("poliza"), poliza));
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

   public List<PolizaDocumento> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back PolizaDocumento entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<PolizaDocumento> getAll()
   {

      CriteriaQuery<PolizaDocumento> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(PolizaDocumento.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(PolizaDocumento.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PolizaDocumentoBean ejbProxy = this.sessionContext.getBusinessObject(PolizaDocumentoBean.class);

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

            return String.valueOf(((PolizaDocumento) value).getIdPolizaDocumento());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private PolizaDocumento add = new PolizaDocumento();

   public PolizaDocumento getAdd()
   {
      return this.add;
   }

   public PolizaDocumento getAdded()
   {
      PolizaDocumento added = this.add;
      this.add = new PolizaDocumento();
      return added;
   }
}
