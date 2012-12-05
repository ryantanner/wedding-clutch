/**************************
* Application
**************************/

(function(win) {

  win.App = Ember.Application.create({
    rootElement: "#weddingClutch",
    ApplicationController: Ember.Controller.extend()
  });

})(window);

(function(app) {

  app.Router = Ember.Router.extend({
    root: Ember.Route.extend({
      index: Ember.Route.extend({
        route: '/',
        connectOutlets: function(router) {
          var controller = router.get('applicationController');
          var context = app.weddingsController;
          controller.connectOutlet('weddings', context);
        }
      })
    })
  });

})(window.App);

/**************************
* Models
**************************/

(function(app) {
  app.Wedding = Em.Object.extend({
    id: null,
    name: null,
    date: null,
    venue: null
  });
})(window.App);

/**************************
* Views
**************************/

(function(app) {
  app.ApplicationView = Ember.View.extend();
})(window.App);

(function(app) {
  app.WeddingsView = Ember.CollectionView.extend({
    contentBinding: 'App.weddingsController.content',
    tagName: 'ul',
    elementId: 'weddingList',
    itemViewClass: Ember.View.extend({
      className: 'wedding-list-item'
    })
  });
})(window.App);

/**************************
* Controllers
**************************/

(function(app) {
  var WeddingsController = Ember.ArrayController.extend({
    content: [],
    currentWedding: null,
    init: function () {
      this._super();

      var me = this;
      $.getJSON('/api/weddings', function(data) {
        me.set('content', []);
        $(data).each(function(index, value) {
          var w = App.Wedding.create({
            id: value.id,
            name: value.name,
            date: value.date,
            venue: value.venue
          });
          me.pushObject(w);
        });
      });
    }
  });

  app.WeddingsController = WeddingsController;
  app.weddingsController = WeddingsController.create();

})(window.App);

App.initialize();
