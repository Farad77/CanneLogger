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

import com.dreams.cannelogger.model.Chauffeur;

/**
 * Backing bean for Chauffeur entities.
 * <p/>
 * This class provides CRUD functionality for all Chauffeur entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ChauffeurBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Chauffeur entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Chauffeur chauffeur;

	public Chauffeur getChauffeur() {
		return this.chauffeur;
	}

	public void setChauffeur(Chauffeur chauffeur) {
		this.chauffeur = chauffeur;
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
			this.chauffeur = this.example;
		} else {
			this.chauffeur = findById(getId());
		}
	}

	public Chauffeur findById(Long id) {

		return this.entityManager.find(Chauffeur.class, id);
	}

	/*
	 * Support updating and deleting Chauffeur entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.chauffeur);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.chauffeur);
				return "view?faces-redirect=true&id=" + this.chauffeur.getId();
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
			Chauffeur deletableEntity = findById(getId());

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
	 * Support searching Chauffeur entities with pagination
	 */

	private int page;
	private long count;
	private List<Chauffeur> pageItems;

	private Chauffeur example = new Chauffeur();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Chauffeur getExample() {
		return this.example;
	}

	public void setExample(Chauffeur example) {
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
		Root<Chauffeur> root = countCriteria.from(Chauffeur.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Chauffeur> criteria = builder
				.createQuery(Chauffeur.class);
		root = criteria.from(Chauffeur.class);
		TypedQuery<Chauffeur> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Chauffeur> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		int roles = this.example.getRoles();
		if (roles != 0) {
			predicatesList.add(builder.equal(root.get("roles"), roles));
		}
		String nom = this.example.getNom();
		if (nom != null && !"".equals(nom)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("nom")),
					'%' + nom.toLowerCase() + '%'));
		}
		String prenom = this.example.getPrenom();
		if (prenom != null && !"".equals(prenom)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("prenom")),
					'%' + prenom.toLowerCase() + '%'));
		}
		String username = this.example.getUsername();
		if (username != null && !"".equals(username)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("username")),
					'%' + username.toLowerCase() + '%'));
		}
		String telephone = this.example.getTelephone();
		if (telephone != null && !"".equals(telephone)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("telephone")),
					'%' + telephone.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Chauffeur> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Chauffeur entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Chauffeur> getAll() {

		CriteriaQuery<Chauffeur> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Chauffeur.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Chauffeur.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ChauffeurBean ejbProxy = this.sessionContext
				.getBusinessObject(ChauffeurBean.class);

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

				return String.valueOf(((Chauffeur) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Chauffeur add = new Chauffeur();

	public Chauffeur getAdd() {
		return this.add;
	}

	public Chauffeur getAdded() {
		Chauffeur added = this.add;
		this.add = new Chauffeur();
		return added;
	}
}
