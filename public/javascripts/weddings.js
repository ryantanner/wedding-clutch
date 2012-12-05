var Wedding = Backbone.Model.extend({

  urlRoot: "/api/weddings",

  defaults: function() {
    return {
      name: "empty wedding",
      date: new Date(),
      venue: "empty venue" 
    }
  },

  initialize: function() {
    if(!this.get('name')) {
      this.set({'name': this.defaults.name});
    }
  },

  clear: function() {
    this.destroy();
  }

});

var WeddingList = Backbone.Collection.extend({

  model: Wedding,

  url: "/api/weddings",

  comparator: function(wedding) {
    return wedding.get('date');
  }

});

var WeddingListView = Backbone.View.extend({

  id: 'wedding-list',

  tagName: 'ul',

  className: 'nav nav-list item-nav-list',

  initialize: function() {
    this.model.bind("reset", this.render, this);
  },

  render: function(eventName) {
    _.each(this.model.models, function(wedding) {
      $(this.el).append(new WeddingListItemView({model:wedding}).render().el)
    }, this);
    return this;
  }

});

var WeddingListItemView = Backbone.View.extend({

  tagName: 'li',

  template: _.template($('#tmpl-wedding-list-item').html()),

  initialize: function() {
    this.model.bind("change", this.render, this);
    this.model.bind("destroy", this.close, this);
  },

  render: function(eventName) {
    this.$el.html(this.template(this.model.toJSON()));
    return this;
  },

  close: function() {
    $el.unbind();
    $el.remove();
  }

});

var AppRouter = Backbone.Router.extend({

  routes: {
    "":"list"
  },

  list: function() {
    this.weddingList = new WeddingList();
    this.weddingListView = new WeddingListView({model: this.weddingList});
    this.weddingList.fetch();
    $("#wedding-list").html(this.weddingListView.render().el);
  }

});

var app = new AppRouter();
Backbone.history.start();




