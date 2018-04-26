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

import com.dreams.cannelogger.model.Planteur;

/**
 * Backing bean for Planteur entities.
 * <p/>
 * This class provides CRUD functionality for all Planteur entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanteurBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Planteur entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Planteur planteur;

	public Planteur getPlanteur() {
		return this.planteur;
	}

	public void setPlanteur(Planteur planteur) {
		this.planteur = planteur;
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
			this.planteur = this.example;
		} else {
			this.planteur = findById(getId());
		}
	}

	public Planteur findById(Long id) {

		return this.entityManager.find(Planteur.class, id);
	}

	/*
	 * Support updating and deleting Planteur entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.planteur);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.planteur);
				return "view?faces-redirect=true&id=" + this.planteur.getId();
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
			Planteur deletableEntity = findById(getId());

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
	 * Support searching Planteur entities with pagination
	 */

	private int page;
	private long count;
	private List<Planteur> pageItems;

	private Planteur example = new Planteur();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Planteur getExample() {
		return this.example;
	}

	public void setExample(Planteur example) {
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
		Root<Planteur> root = countCriteria.from(Planteur.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Planteur> criteria = builder.createQuery(Planteur.class);
		root = criteria.from(Planteur.class);
		TypedQuery<Planteur> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Planteur> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String numeroPlanteur = this.example.getNumeroPlanteur();
		if (numeroPlanteur != null && !"".equals(numeroPlanteur)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("numeroPlanteur")),
					'%' + numeroPlanteur.toLowerCase() + '%'));
		}
		String nomResponsable = this.example.getNomResponsable();
		if (nomResponsable != null && !"".equals(nomResponsable)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("nomResponsable")),
					'%' + nomResponsable.toLowerCase() + '%'));
		}
		String nomSocial = this.example.getNomSocial();
		if (nomSocial != null && !"".equals(nomSocial)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("nomSocial")),
					'%' + nomSocial.toLowerCase() + '%'));
		}
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

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Planteur> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Planteur entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Planteur> getAll() {

		CriteriaQuery<Planteur> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Planteur.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Planteur.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PlanteurBean ejbProxy = this.sessionContext
				.getBusinessObject(PlanteurBean.class);

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

				return String.valueOf(((Planteur) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Planteur add = new Planteur();

	public Planteur getAdd() {
		return this.add;
	}

	public Planteur getAdded() {
		Planteur added = this.add;
		this.add = new Planteur();
		return added;
	}
}
