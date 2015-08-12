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

import org.seguro.model.Siniestro;
import java.util.Iterator;
import org.seguro.model.CoberturaSiniestro;
import org.seguro.model.Poliza;
import org.seguro.model.RecuperacionMedica;
import org.seguro.model.RestauracionMecanica;
import org.seguro.model.SiniestroDocumento;
import org.seguro.model.SiniestroFoto;

/**
 * Backing bean for Siniestro entities.
 * <p/>
 * This class provides CRUD functionality for all Siniestro entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SiniestroBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Siniestro entities
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

   private Siniestro siniestro;

   public Siniestro getSiniestro()
   {
      return this.siniestro;
   }

   public void setSiniestro(Siniestro siniestro)
   {
      this.siniestro = siniestro;
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
         this.siniestro = this.example;
      }
      else
      {
         this.siniestro = findById(getId());
      }
   }

   public Siniestro findById(Integer id)
   {

      return this.entityManager.find(Siniestro.class, id);
   }

   /*
    * Support updating and deleting Siniestro entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.siniestro);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.siniestro);
            return "view?faces-redirect=true&id=" + this.siniestro.getIdSiniestro();
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
         Siniestro deletableEntity = findById(getId());
         Poliza poliza = deletableEntity.getPoliza();
         poliza.getSiniestros().remove(deletableEntity);
         deletableEntity.setPoliza(null);
         this.entityManager.merge(poliza);
         Iterator<SiniestroDocumento> iterSiniestroDocumentos = deletableEntity.getSiniestroDocumentos().iterator();
         for (; iterSiniestroDocumentos.hasNext();)
         {
            SiniestroDocumento nextInSiniestroDocumentos = iterSiniestroDocumentos.next();
            nextInSiniestroDocumentos.setSiniestro(null);
            iterSiniestroDocumentos.remove();
            this.entityManager.merge(nextInSiniestroDocumentos);
         }
         Iterator<RestauracionMecanica> iterRestauracionMecanicas = deletableEntity.getRestauracionMecanicas().iterator();
         for (; iterRestauracionMecanicas.hasNext();)
         {
            RestauracionMecanica nextInRestauracionMecanicas = iterRestauracionMecanicas.next();
            nextInRestauracionMecanicas.setSiniestro(null);
            iterRestauracionMecanicas.remove();
            this.entityManager.merge(nextInRestauracionMecanicas);
         }
         Iterator<CoberturaSiniestro> iterCoberturaSiniestros = deletableEntity.getCoberturaSiniestros().iterator();
         for (; iterCoberturaSiniestros.hasNext();)
         {
            CoberturaSiniestro nextInCoberturaSiniestros = iterCoberturaSiniestros.next();
            nextInCoberturaSiniestros.setSiniestro(null);
            iterCoberturaSiniestros.remove();
            this.entityManager.merge(nextInCoberturaSiniestros);
         }
         Iterator<RecuperacionMedica> iterRecuperacionMedicas = deletableEntity.getRecuperacionMedicas().iterator();
         for (; iterRecuperacionMedicas.hasNext();)
         {
            RecuperacionMedica nextInRecuperacionMedicas = iterRecuperacionMedicas.next();
            nextInRecuperacionMedicas.setSiniestro(null);
            iterRecuperacionMedicas.remove();
            this.entityManager.merge(nextInRecuperacionMedicas);
         }
         Iterator<SiniestroFoto> iterSiniestroFotos = deletableEntity.getSiniestroFotos().iterator();
         for (; iterSiniestroFotos.hasNext();)
         {
            SiniestroFoto nextInSiniestroFotos = iterSiniestroFotos.next();
            nextInSiniestroFotos.setSiniestro(null);
            iterSiniestroFotos.remove();
            this.entityManager.merge(nextInSiniestroFotos);
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
    * Support searching Siniestro entities with pagination
    */

   private int page;
   private long count;
   private List<Siniestro> pageItems;

   private Siniestro example = new Siniestro();

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

   public Siniestro getExample()
   {
      return this.example;
   }

   public void setExample(Siniestro example)
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
      Root<Siniestro> root = countCriteria.from(Siniestro.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Siniestro> criteria = builder.createQuery(Siniestro.class);
      root = criteria.from(Siniestro.class);
      TypedQuery<Siniestro> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Siniestro> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idSiniestro = this.example.getIdSiniestro();
      if (idSiniestro != 0)
      {
         predicatesList.add(builder.equal(root.get("idSiniestro"), idSiniestro));
      }
      Poliza poliza = this.example.getPoliza();
      if (poliza != null)
      {
         predicatesList.add(builder.equal(root.get("poliza"), poliza));
      }
      String nombreCliente = this.example.getNombreCliente();
      if (nombreCliente != null && !"".equals(nombreCliente))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombreCliente")), '%' + nombreCliente.toLowerCase() + '%'));
      }
      String direccion = this.example.getDireccion();
      if (direccion != null && !"".equals(direccion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("direccion")), '%' + direccion.toLowerCase() + '%'));
      }
      Integer telefono = this.example.getTelefono();
      if (telefono != null && telefono.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("telefono"), telefono));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Siniestro> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Siniestro entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Siniestro> getAll()
   {

      CriteriaQuery<Siniestro> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Siniestro.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Siniestro.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SiniestroBean ejbProxy = this.sessionContext.getBusinessObject(SiniestroBean.class);

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

            return String.valueOf(((Siniestro) value).getIdSiniestro());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Siniestro add = new Siniestro();

   public Siniestro getAdd()
   {
      return this.add;
   }

   public Siniestro getAdded()
   {
      Siniestro added = this.add;
      this.add = new Siniestro();
      return added;
   }
}
