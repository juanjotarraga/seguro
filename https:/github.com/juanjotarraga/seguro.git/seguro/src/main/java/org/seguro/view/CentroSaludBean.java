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

import org.seguro.model.CentroSalud;
import java.util.Iterator;
import org.seguro.model.RecuperacionMedica;

/**
 * Backing bean for CentroSalud entities.
 * <p/>
 * This class provides CRUD functionality for all CentroSalud entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CentroSaludBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving CentroSalud entities
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

   private CentroSalud centroSalud;

   public CentroSalud getCentroSalud()
   {
      return this.centroSalud;
   }

   public void setCentroSalud(CentroSalud centroSalud)
   {
      this.centroSalud = centroSalud;
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
         this.centroSalud = this.example;
      }
      else
      {
         this.centroSalud = findById(getId());
      }
   }

   public CentroSalud findById(Integer id)
   {

      return this.entityManager.find(CentroSalud.class, id);
   }

   /*
    * Support updating and deleting CentroSalud entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.centroSalud);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.centroSalud);
            return "view?faces-redirect=true&id=" + this.centroSalud.getIdCentroSalud();
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
         CentroSalud deletableEntity = findById(getId());
         Iterator<RecuperacionMedica> iterRecuperacionMedicas = deletableEntity.getRecuperacionMedicas().iterator();
         for (; iterRecuperacionMedicas.hasNext();)
         {
            RecuperacionMedica nextInRecuperacionMedicas = iterRecuperacionMedicas.next();
            nextInRecuperacionMedicas.setCentroSalud(null);
            iterRecuperacionMedicas.remove();
            this.entityManager.merge(nextInRecuperacionMedicas);
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
    * Support searching CentroSalud entities with pagination
    */

   private int page;
   private long count;
   private List<CentroSalud> pageItems;

   private CentroSalud example = new CentroSalud();

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

   public CentroSalud getExample()
   {
      return this.example;
   }

   public void setExample(CentroSalud example)
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
      Root<CentroSalud> root = countCriteria.from(CentroSalud.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<CentroSalud> criteria = builder.createQuery(CentroSalud.class);
      root = criteria.from(CentroSalud.class);
      TypedQuery<CentroSalud> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<CentroSalud> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCentroSalud = this.example.getIdCentroSalud();
      if (idCentroSalud != 0)
      {
         predicatesList.add(builder.equal(root.get("idCentroSalud"), idCentroSalud));
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

   public List<CentroSalud> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back CentroSalud entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<CentroSalud> getAll()
   {

      CriteriaQuery<CentroSalud> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(CentroSalud.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(CentroSalud.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CentroSaludBean ejbProxy = this.sessionContext.getBusinessObject(CentroSaludBean.class);

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

            return String.valueOf(((CentroSalud) value).getIdCentroSalud());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private CentroSalud add = new CentroSalud();

   public CentroSalud getAdd()
   {
      return this.add;
   }

   public CentroSalud getAdded()
   {
      CentroSalud added = this.add;
      this.add = new CentroSalud();
      return added;
   }
}
