# Lint-MVP
LINT rules for valodating MVP contracts in Android Studio.

Method names can easily indicate misunderstading of MVP concepts. So a few simple LINT rules can provide a lot of help for begginers in Android MVP. 

This repository provides rules for any interface ending with "View" or "Presenter" or any class implementing such interface.

## Rules

Rules for View methods:
  - Not starting with "on" prefix. Warning: **"View should implement strict commands, not reacting for events. Avoid onXXX methods. Try use showXXX notation."**. 
  
Rules for Presenter methods:
 - Not starting with such prefixes: "get", "is", "should", "was",  . Warning: **"Presenter should not leak business logic. Getters are not recommended."**.
 - Not being names as "onStart", "onStop", "onResume", "onPause", "onCreate", "onViewCreated", "onCreateView", "onDestroy"  Warning: **"Presenter should not implement activity lifecycle methods. Consider only base onAttach and onDetach methods."**.


# License

MIT License

