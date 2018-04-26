package com.dreams.cannelogger.view;

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

import com.dreams.cannelogger.model.Ticket;
import com.dreams.cannelogger.model.Chargeur;
import com.dreams.cannelogger.model.Chauffeur;
import com.dreams.cannelogger.model.Coupeur;

/**
 * Backing bean for Ticket entities.
 * <p/>
 * This class provides CRUD functionality for all Ticket entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TicketBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Ticket entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Ticket ticket;

	public Ticket getTicket() {
		return this.ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "CanneLogger-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.ticket = this.example;
		} else {
			this.ticket = findById(getId());
		}
	}

	public Ticket findById(Long id) {

		return this.entityManager.find(Ticket.class, id);
	}

	/*
	 * Support updating and deleting Ticket entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.ticket);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.ticket);
				return "view?faces-redirect=true&id=" + this.ticket.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Ticket deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Ticket entities with pagination
	 */

	private int page;
	private long count;
	private List<Ticket> pageItems;

	private Ticket example = new Ticket();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Ticket getExample() {
		return this.example;
	}

	public void setExample(Ticket example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Ticket> root = countCriteria.from(Ticket.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Ticket> criteria = builder.createQuery(Ticket.class);
		root = criteria.from(Ticket.class);
		TypedQuery<Ticket> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Ticket> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		int ctics = this.example.getCtics();
		if (ctics != 0) {
			predicatesList.add(builder.equal(root.get("ctics"), ctics));
		}
		Coupeur coupeur = this.example.getCoupeur();
		if (coupeur != null) {
			predicatesList.add(builder.equal(root.get("coupeur"), coupeur));
		}
		String details = this.example.getDetails();
		if (details != null && !"".equals(details)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("details")),
					'%' + details.toLowerCase() + '%'));
		}
		Chauffeur chauffeur = this.example.getChauffeur();
		if (chauffeur != null) {
			predicatesList.add(builder.equal(root.get("chauffeur"), chauffeur));
		}
		Chargeur chargeur = this.example.getChargeur();
		if (chargeur != null) {
			predicatesList.add(builder.equal(root.get("chargeur"), chargeur));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Ticket> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Ticket entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Ticket> getAll() {

		CriteriaQuery<Ticket> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Ticket.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Ticket.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final TicketBean ejbProxy = this.sessionContext
				.getBusinessObject(TicketBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Ticket) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Ticket add = new Ticket();

	public Ticket getAdd() {
		return this.add;
	}

	public Ticket getAdded() {
		Ticket added = this.add;
		this.add = new Ticket();
		return added;
	}
}
