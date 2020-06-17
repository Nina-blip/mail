package be.vdab.mail.services;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.mailing.LidMailing;
import be.vdab.mail.repositories.LidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultLidService implements LidService{
    private final LidRepository repository;
    private final LidMailing mailing;

    public DefaultLidService(LidRepository repository, LidMailing mailing) {
        this.repository = repository;
        this.mailing = mailing;
    }


    @Override
    @Transactional
    public void registreer(Lid lid, String ledenURL) {
        repository.save(lid);
        mailing.stuurMailNaRegistratie(lid, ledenURL);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lid> findById(long id) {
        return repository.findById(id);
    }
}
