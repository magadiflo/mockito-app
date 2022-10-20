package org.magadiflo.mockito.app.services;

import org.magadiflo.mockito.app.models.Examen;

public interface IExamenService {

    Examen findExamenByNombre(String nombre);

}
