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

import org.seguro.model.Cliente;
import java.util.Iterator;
import org.seguro.model.Poliza;

/**
 * Backing bean for Cliente entities.
 * <p/>
 * This class provides CRUD functionality for all Cliente entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ClienteBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Cliente entities
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

   private Cliente cliente;

   public Cliente getCliente()
   {
      return this.cliente;
   }

   public void setCliente(Cliente cliente)
   {
      this.cliente = cliente;
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
         this.cliente = this.example;
      }
      else
      {
         this.cliente = findById(getId());
      }
   }

   public Cliente findById(Integer id)
   {

      return this.entityManager.find(Cliente.class, id);
   }

   /*
    * Support updating and deleting Cliente entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.cliente);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.cliente);
            return "view?faces-redirect=true&id=" + this.cliente.getIdCliente();
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
         Cliente deletableEntity = findById(getId());
         Iterator<Poliza> iterPolizas = deletableEntity.getPolizas().iterator();
         for (; iterPolizas.hasNext();)
         {
            Poliza nextInPolizas = iterPolizas.next();
            nextInPolizas.setCliente(null);
            iterPolizas.remove();
            this.entityManager.merge(nextInPolizas);
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
    * Support searching Cliente entities with pagination
    */

   private int page;
   private long count;
   private List<Cliente> pageItems;

   private Cliente example = new Cliente();

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

   public Cliente getExample()
   {
      return this.example;
   }

   public void setExample(Cliente example)
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
      Root<Cliente> root = countCriteria.from(Cliente.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Cliente> criteria = builder.createQuery(Cliente.class);
      root = criteria.from(Cliente.class);
      TypedQuery<Cliente> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Cliente> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idCliente = this.example.getIdCliente();
      if (idCliente != 0)
      {
         predicatesList.add(builder.equal(root.get("idCliente"), idCliente));
      }
      String nombres = this.example.getNombres();
      if (nombres != null && !"".equals(nombres))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombres")), '%' + nombres.toLowerCase() + '%'));
      }
      String apellidos = this.example.getApellidos();
      if (apellidos != null && !"".equals(apellidos))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("apellidos")), '%' + apellidos.toLowerCase() + '%'));
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

   public List<Cliente> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Cliente entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Cliente> getAll()
   {

      CriteriaQuery<Cliente> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Cliente.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Cliente.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ClienteBean ejbProxy = this.sessionContext.getBusinessObject(ClienteBean.class);

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

            return String.valueOf(((Cliente) value).getIdCliente());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Cliente add = new Cliente();

   public Cliente getAdd()
   {
      return this.add;
   }

   public Cliente getAdded()
   {
      Cliente added = this.add;
      this.add = new Cliente();
      return added;
   }
}
