package org.sid.services;

import org.sid.entities.Avis;
import org.sid.entities.Freelancer;
import org.sid.entities.Particulier;

public interface ServiceEvaluation {
	public void AjouterAvis(Avis avis, Freelancer freelancer);

	public void AjouterAvis(Avis avis, Particulier particulier);

	public void DonnerNote(Freelancer freelancer, Byte note);

	public Double RecalculerMoyenne(Freelancer freelancer);
}